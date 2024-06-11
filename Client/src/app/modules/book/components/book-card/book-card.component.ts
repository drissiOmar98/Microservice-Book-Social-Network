import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponse} from "../../../../services/book-service/models/book-response";

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book: BookResponse = {};
  private _manage = false;
  private _bookCover: string | undefined;

  /*get bookCover(): string | undefined {
    if (this._book.cover) {
      return 'data:image/jpg;base64,' + this._book.cover
    }
    return 'https://source.unsplash.com/user/c_v_r/1900x800';
  }*/

  // Method to get the book cover image
  get bookCover(): string | undefined {
    if (this._book.cover && typeof this._book.cover === 'string') {
      // Try to determine the MIME type dynamically if not provided
      const mimeType = this.getMimeTypeFromBase64(this._book.cover);
      return `data:${mimeType};base64,` + this._book.cover;
    }
    return 'https://source.unsplash.com/user/c_v_r/1900x800';
  }

// Method to determine the MIME type from the base64 string
  private getMimeTypeFromBase64(base64: string): string {
    const firstChar = base64.charAt(0);
    switch (firstChar) {
      case '/':
        return 'image/jpeg';
      case 'i':
        return 'image/png';
      case 'R':
        return 'image/gif';
      case 'U':
        return 'image/webp';
      default:
        return 'image/jpeg'; // Fallback to jpeg if unknown
    }
  }


  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }


  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  onShare() {
    this.share.emit(this._book);
  }

  onArchive() {
    this.archive.emit(this._book);
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this._book);
  }

  onBorrow() {
    this.borrow.emit(this._book);
  }

  onEdit() {
    this.edit.emit(this._book);
  }

  onShowDetails() {
    this.details.emit(this._book);
  }


}
