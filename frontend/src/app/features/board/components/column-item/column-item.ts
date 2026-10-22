import { CdkDragHandle } from '@angular/cdk/drag-drop';
import {
  Component,
  ElementRef,
  HostListener,
  Injector,
  afterNextRender,
  inject,
  input,
  output,
  signal,
  viewChild,
} from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { tablerDots, tablerGripVertical } from '@ng-icons/tabler-icons';
import { ConfirmModal } from '../../../../components/global/confirm-modal/confirm-modal';
import { BoardColumnResponse } from '../../../../models/column';
import { ColumnService } from '../../../../service/column/column.service';
import { ColumnMenu } from '../column-menu/column-menu';
import { ColumnMenuService } from '../column-menu/column-menu.service';

@Component({
  imports: [NgIcon, ColumnMenu, ConfirmModal, CdkDragHandle],
  providers: [provideIcons({ tablerGripVertical, tablerDots })],
  selector: 'app-column-item',
  templateUrl: './column-item.html',
})
export class ColumnItem {
  /* ---------- Inputs / Outputs ---------- */
  column = input<BoardColumnResponse | null>(null);
  boardId = input<string | null>(null);
  renamed = output<BoardColumnResponse>();
  deleted = output<void>();

  /* ---------- Dependencies ---------- */
  private menuService = inject(ColumnMenuService);
  private columnService = inject(ColumnService);
  private injector = inject(Injector);

  /* ---------- Queries ---------- */
  private menuWrapper = viewChild<ElementRef<HTMLElement>>('menuWrapper');
  private nameInput = viewChild<ElementRef<HTMLInputElement>>('nameInput');

  /* ---------- Menu ---------- */
  get isMenuOpen() {
    const id = this.column()?.id;
    return id != null && this.menuService.isOpen(id);
  }

  toggleMenu() {
    const id = this.column()?.id;
    if (id != null) this.menuService.toggle(id);
  }

  // Close when clicking outside this column
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    if (!this.isMenuOpen) return;

    const wrapper = this.menuWrapper()?.nativeElement;
    if (wrapper && !wrapper.contains(event.target as Node)) {
      this.menuService.close();
    }
  }

  // Close with Escape
  @HostListener('document:keydown.escape')
  onEscape() {
    if (this.isMenuOpen) this.menuService.close();
  }

  /* ---------- Update column name ---------- */
  protected readonly updateNameInputVisible = signal(false);

  showUpdateNameInput(): void {
    this.updateNameInputVisible.set(true);
    this.menuService.close();

    afterNextRender(
      () => {
        const el = this.nameInput()?.nativeElement;
        el?.focus();
        el?.select();
      },
      { injector: this.injector },
    );
  }

  cancelUpdateName(): void {
    this.updateNameInputVisible.set(false);
  }

  saveName(value: string): void {
    // Prevents double save (Enter then blur) and save after Escape
    if (!this.updateNameInputVisible()) return;
    this.updateNameInputVisible.set(false);

    const name = value.trim();
    const column = this.column();
    const boardId = this.boardId();

    if (!name || !column || !boardId || name === column.name) return;

    this.columnService.updateColumnName(boardId, column.id, { name }).subscribe({
      next: (updated) => this.renamed.emit(updated),
      error: (err) => console.error('Failed to update column name', err),
    });
  }

  /* ---------- Delete modal ---------- */
  protected readonly showConfirmModal = signal(false);

  openDeleteModal() {
    this.menuService.close();
    this.showConfirmModal.set(true);
  }

  closeDeleteModal() {
    this.showConfirmModal.set(false);
  }

  deleteColumn(columnId: string | undefined) {
    const boardId = this.boardId();
    if (!boardId) throw new Error('Cannot determine board id');
    if (!columnId) throw new Error('Cannot determine column id');

    this.columnService.deleteColumn(boardId, columnId).subscribe({
      next: () => {
        this.closeDeleteModal();
        this.deleted.emit();
      },
      error: (err) => console.error('Failed to delete column', err),
    });
  }
}