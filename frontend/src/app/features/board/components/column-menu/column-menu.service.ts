import { Service, signal } from '@angular/core';

@Service()
export class ColumnMenuService {
  readonly openMenuId = signal<number | string | null>(null);

  isOpen(id: number | string) {
    return this.openMenuId() === id;
  }

  toggle(id: number | string) {
    this.openMenuId.update((current) => (current === id ? null : id));
  }

  close() {
    this.openMenuId.set(null);
  }
}