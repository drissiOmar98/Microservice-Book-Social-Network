import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/book-service/services/book.service";
import {Router} from "@angular/router";
import {PageResponseBookResponse} from "../../../../services/book-service/models/page-response-book-response";
import {BookResponse} from "../../../../services/book-service/models/book-response";
import {
  BookTransactionHistoryService
} from "../../../../services/transaction-service/services/book-transaction-history.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  pages: any = [];
  page= 0;
  size= 4;
  message = '';
  bookResponse: PageResponseBookResponse = {};
  level: 'success' |'error' = 'success';

  constructor(
    private transactionHistoryService: BookTransactionHistoryService,
    private bookService: BookService,
    private router: Router,
    private toastService: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.findAllBooks();

  }


  private findAllBooks() {
    this.bookService.findAllBooks({
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


  borrowBook(book: BookResponse) {
    this.message = '';
    this.level = 'success';
    this.transactionHistoryService.borrowBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        /*this.level = 'success';
        this.message = 'Book successfully added to your list';*/
        this.toastService.success('Book successfully added to your list', 'Done!')
      },
      error: (err) => {
        /*console.log(err);
        this.level = 'error';
        this.message = err.error.error;*/
        this.toastService.error(err.error.error, 'Oups!!')
      }
    });
  }
}
