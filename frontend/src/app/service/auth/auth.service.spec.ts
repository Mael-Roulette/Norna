import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './auth.interceptor';
import { UserResponse, UserSigninRequest, UserSignupRequest } from '../../models/user';
import { AuthResult } from '../../models/auth';

// Test data
const mockSignupRequest: UserSignupRequest = {
  username: 'test',
  email: 'test@example.com',
  password: 'Password123!',
} as UserSignupRequest;

const mockSigninRequest: UserSigninRequest = {
  email: 'test@example.com',
  password: 'Password123!',
} as UserSigninRequest;

const mockUserResponse: UserResponse = {
  username: 'test',
  email: 'test@example.com',
  createdAt: new Date(),
} as UserResponse;

const mockAuthResult: AuthResult = {
  token: 'fake-jwt-token',
  expiresIn: 3600, // seconds
} as AuthResult;

describe('AuthService', () => {
  let service: AuthService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    // If we don't fake the clock, the expected
    // "expires_at" value we assert on later would be a moving target and the
    // test could be wrong depending on when it runs.
    vi.useFakeTimers();
    vi.setSystemTime(new Date('2026-01-01T00:00:00Z'));

    localStorage.clear();

    // provideHttpClientTesting() swaps the real HTTP backend for a fake one
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(AuthService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // httpTesting.verify() throws if there are any requests that were made but never matched or flushed by a test
    httpTesting.verify();

    localStorage.clear();
    vi.useRealTimers();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /* -------------------------------------------------- */
  /* ---------- Test signup request ---------- */

  describe('signupUser', () => {
    /* ----- Test successful signup request ----- */
    it('should POST to /auth/signup with the correct body, headers and credentials', () => {
      let result: UserResponse | undefined;

      // Send the request to the fake backend
      service.signupUser(mockSignupRequest).subscribe((res) => (result = res));

      const req = httpTesting.expectOne(`${service.baseUrl}/auth/signup`);

      // Verify the request was built correctly
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockSignupRequest);
      expect(req.request.headers.get('Content-type')).toBe('application/json');
      expect(req.request.withCredentials).toBe(true);

      // Simulate the server responding successfully
      req.flush(mockUserResponse);

      // Verify the request result is equal to our test data
      expect(result).toEqual(mockUserResponse);
    });

    /* ----- Test unsuccessful signup request ----- */
    it('should propagate an error response', () => {
      let error: unknown;

      service.signupUser(mockSignupRequest).subscribe({
        next: () => {},
        error: (err) => (error = err),
      });

      const req = httpTesting.expectOne(`${service.baseUrl}/auth/signup`);

      // Simulate the server returning a 409 Conflict for a duplicate email
      req.flush({ message: 'Email is already in use' }, { status: 409, statusText: 'conflict' });

      expect(error).toBeTruthy();
    });
  });

  /* -------------------------------------------------- */
  /* ---------- Test signin request ---------- */

  describe('signinUser', () => {
    /* ----- Test successful signin request ----- */
    it('should POST to /auth/signin with the correct body, headers and credentials', () => {
      let result: AuthResult | undefined;

      // Send the request to the fake backend
      service.signinUser(mockSigninRequest).subscribe((res) => (result = res));

      const req = httpTesting.expectOne(`${service.baseUrl}/auth/signin`);

      // Verify the request was build correctly
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockSigninRequest);
      expect(req.request.headers.get('Content-type')).toBe('application/json');
      expect(req.request.withCredentials).toBe(true);

      // Simulate the server responding successfully
      req.flush(mockAuthResult);

      // Verify the request result is equal to our test data
      expect(result).toEqual(mockAuthResult);

      // Check that setSession() have written the token in the local storage
      expect(localStorage.getItem('id_token')).toBe(mockAuthResult.token);

      // Check that expires_at is good
      const expectedExpiresAt = new Date('2026-01-01T01:00:00Z').getTime();
      expect(JSON.parse(localStorage.getItem('expires_at')!)).toBe(expectedExpiresAt);
    });

    /* ----- Test unsuccessful signin request ----- */
    it('should not store a session', () => {
      service.signinUser(mockSigninRequest).subscribe({
        next: () => {},
        error: () => {},
      });

      const req = httpTesting.expectOne(`${service.baseUrl}/auth/signin`);

      // Simulate the server returning an error with status 401
      req.flush({ message: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });

      // the token must be null if the signin request failed
      expect(localStorage.getItem('id_token')).toBeNull();
    });
  });

  /* -------------------------------------------------- */
  /* ---------- Test logout ---------- */
  describe('logout', () => {
    it('should clear the session from the local storage', () => {
      // Pretend the user is already logged in
      localStorage.setItem('id_token', 'fake-token');
      localStorage.setItem('expires_at', JSON.stringify(Date.now() + 10000));

      service.logout();

      // The logout function should remove both key from the local storage
      expect(localStorage.getItem('id_token')).toBeNull();
      expect(localStorage.getItem('expires_at')).toBeNull();
    });
  });

  /* -------------------------------------------------- */
  /* ---------- Test verify the user condition ---------- */
  describe('isLoggedIn / isLoggedOut / getExpiration', () => {
    it('should report logged in when expiry is in the future', () => {
      const future = Date.now() + 60_000; // 60s from "now"
      localStorage.setItem('expires_at', JSON.stringify(future));

      expect(service.isLoggedIn()).toBe(true);
      expect(service.isLoggedOut()).toBe(false);
    });

    it('should report logged out when expires_at is in the past', () => {
      const past = Date.now() - 60_000; // 60s before now
      localStorage.setItem('expires_at', JSON.stringify(past));

      expect(service.isLoggedIn()).toBe(false);
      expect(service.isLoggedOut()).toBe(true);
    });

    it('should report logged out when there is no stored expiry', () => {
      // No 'expires_at' key at all so getExpiration() falls back to moment(0)
      expect(service.isLoggedIn()).toBe(false);
      expect(service.getExpiration().valueOf()).toBe(0);
    });
  });
});
