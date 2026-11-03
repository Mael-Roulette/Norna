import { NativeDateAdapter } from '@angular/material/core';

export class CustomDateAdapter extends NativeDateAdapter {
  override parse(value: unknown): Date | null {
    if (typeof value !== 'string') return value ? new Date(value as any) : null;
    const m = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/.exec(value.trim());
    if (!m) return null;
    const [, d, mo, y] = m.map(Number) as unknown as number[];
    const date = new Date(y, mo - 1, d); // local midnight
    // reject overflow like 31/02/2026
    return date.getFullYear() === y && date.getMonth() === mo - 1 && date.getDate() === d
      ? date
      : null;
  }

  override format(date: Date, displayFormat: any): string {
    if (!this.isValid(date)) return '';
    if (displayFormat === 'dd/MM/yyyy') {
      const dd = String(date.getDate()).padStart(2, '0');
      const mm = String(date.getMonth() + 1).padStart(2, '0');
      return `${dd}/${mm}/${date.getFullYear()}`;
    }
    return super.format(date, displayFormat);
  }
  
  override getFirstDayOfWeek(): number {
    return 1; // Monday
  }
}