import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/book-service/services/book.service";
import {FeedbackService} from "../../../../services/feedback-service/services/feedback.service";
import {
  BookTransactionHistoryService
} from "../../../../services/transaction-service/services/book-transaction-history.service";
import {
  PageResponseBorrowedBookResponse
} from "../../../../services/transaction-service/models/page-response-borrowed-book-response";
import {BorrowedBookResponse} from "../../../../services/transaction-service/models/borrowed-book-response";

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.scss'
})
export class ReturnedBooksComponent implements OnInit {

  page = 0;
  size = 5;
  message = '';
  level: 'success' |'error' = 'success';
  pages: any = [];
  returnedBooks: PageResponseBorrowedBookResponse = {};

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  constructor(
    private bookService: BookService,
    private feedbackService: FeedbackService,
    private transactionService: BookTransactionHistoryService,
  ) {
  }

  private findAllReturnedBooks() {
    this.transactionService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp) => {
        this.returnedBooks = resp;
        this.pages = Array(this.returnedBooks.totalPages)
          .fill(0)
          .map((x, i) => i);
      }
    });
  }


  gotToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  get isLastPage() {
    return this.page === this.returnedBooks.totalPages as number - 1;
  }


  approveBookReturn(book: BorrowedBookResponse) {
    if (!book.returned) {
      return;
    }
    this.transactionService.approveReturnBorrowBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Book return approved';
        this.findAllReturnedBooks();
      }
    });
  }
}
