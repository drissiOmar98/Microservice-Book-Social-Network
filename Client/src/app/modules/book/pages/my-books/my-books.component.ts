import {Component, OnInit} from '@angular/core';
import {PageResponseBookResponse} from "../../../../services/book-service/models/page-response-book-response";
import {
  BookTransactionHistoryService
} from "../../../../services/transaction-service/services/book-transaction-history.service";
import {BookService} from "../../../../services/book-service/services/book.service";
import {Router} from "@angular/router";
import {BookResponse} from "../../../../services/book-service/models/book-response";

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit{
  pages: any = [];
  page= 0;
  size= 5;

  bookResponse: PageResponseBookResponse = {};


  constructor(
    private transactionHistoryService: BookTransactionHistoryService,
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllBooks();

  }


  private findAllBooks() {
    this.bookService.findAllBooksByOwner({
      page: this.page,
      size: this.size
    })
      .subscribe({
        next: (books) => {
          console.log("books=",books)
          this.bookResponse = books;
          this.pages = Array(this.bookResponse.totalPages)
            .fill(0)
            .map((x, i) => i);
        }
      });
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }


  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }


  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  get isLastPage() {
    return this.page === this.bookResponse.totalPages as number - 1;
  }


  displayBookDetails(book: BookResponse) {
    this.router.navigate(['books', 'details', book.id]);
  }


  archiveBook(book: BookResponse) {
    this.bookService.updateArchivedStatus({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        book.archived = !book.archived;
      }
    });
  }
  shareBook(book: BookResponse) {
    this.bookService.updateShareableStatus({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        book.shareable = !book.shareable;
      }
    });
  }

  editBook(book: BookResponse) {
    this.router.navigate(['books','manage',book.id])
  }
}
