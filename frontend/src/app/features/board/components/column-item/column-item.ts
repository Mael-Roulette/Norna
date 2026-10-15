import { Component, ElementRef, HostListener, inject, input, signal, viewChild } from '@angular/core';
import { BoardColumnResponse } from '../../../../models/column';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { tablerGripVertical, tablerDots } from '@ng-icons/tabler-icons';
import { ColumnMenu } from '../column-menu/column-menu';
import { ColumnMenuService } from '../column-menu/column-menu.service';
import { ConfirmModal } from '../../../../components/global/confirm-modal/confirm-modal';
import { ColumnService } from '../../../../service/column/column.service';

@Component({
  imports: [NgIcon, ColumnMenu, ConfirmModal],
  providers: [provideIcons({ tablerGripVertical, tablerDots })],
  selector: 'app-column-item',
  templateUrl: './column-item.html',
})
export class ColumnItem {
  column = input<BoardColumnResponse | null>(null);

  private menuService = inject(ColumnMenuService);
  private columnService = inject(ColumnService);
  private menuWrapper = viewChild<ElementRef<HTMLElement>>('menuWrapper');

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

  /* ----------------------------------- */
  /* ---------- Delete modal ---------- */
  protected readonly showConfirmModal = signal<boolean>(false);
  boardId = input<string | null>(null);

  openDeleteModal () {
    this.menuService.close();
    this.showConfirmModal.set(true);
  }

  closeDeleteModal() {
    this.showConfirmModal.set(false);
  }

  deleteColumn(columnId: string | undefined) {
    const boardId = this.boardId();
    if (!boardId) {
      throw new Error("Cannot determine board id");
    }

    if (!columnId) {
      throw new Error("Cannot determine column id");
    }

    this.columnService.deleteColumn(boardId, columnId);
  }
}