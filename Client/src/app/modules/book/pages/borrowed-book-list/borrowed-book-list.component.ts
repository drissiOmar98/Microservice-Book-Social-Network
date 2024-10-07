import {Component, OnInit} from '@angular/core';
import {
  PageResponseBorrowedBookResponse
} from "../../../../services/transaction-service/models/page-response-borrowed-book-response";
import {BookService} from "../../../../services/book-service/services/book.service";
import {FeedbackService} from "../../../../services/feedback-service/services/feedback.service";
import {
  BookTransactionHistoryService
} from "../../../../services/transaction-service/services/book-transaction-history.service";
import {BookResponse} from "../../../../services/book-service/models/book-response";
import {FeedbackRequest} from "../../../../services/feedback-service/models/feedback-request";
import {BorrowedBookResponse} from "../../../../services/transaction-service/models/borrowed-book-response";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent  implements OnInit {

  page = 0;
  size = 5;
  pages: any = [];
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  selectedBook: BookResponse | undefined = undefined;
  feedbackRequest: FeedbackRequest = {bookId: 0, comment: '', note: 0};

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  constructor(
    private bookService: BookService,
    private toastService: ToastrService,
    private feedbackService: FeedbackService,
    private transactionService: BookTransactionHistoryService,
  ) {
  }


  private findAllBorrowedBooks() {
    this.transactionService.findAllBorrowedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp) => {
        this.borrowedBooks = resp;
        this.pages = Array(this.borrowedBooks.totalPages)
          .fill(0)
          .map((x, i) => i);
      }
    });
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllBorrowedBooks();
  }

  goToLastPage() {
    this.page = this.borrowedBooks.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  get isLastPage() {
    return this.page === this.borrowedBooks.totalPages as number - 1;
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }

  returnBook(withFeedback: boolean) {
    this.transactionService.returnBorrowBook({
      'book-id': this.selectedBook?.id as number
    }).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.toastService.success('Book has been returned and the owner is notified', 'Success');
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    });
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {
      }
    });
  }



}
