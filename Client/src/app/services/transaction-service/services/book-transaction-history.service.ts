/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { approveReturnBorrowBook } from '../fn/book-transaction-history/approve-return-borrow-book';
import { ApproveReturnBorrowBook$Params } from '../fn/book-transaction-history/approve-return-borrow-book';
import { BookTransactionHistory } from '../models/book-transaction-history';
import { borrowBook } from '../fn/book-transaction-history/borrow-book';
import { BorrowBook$Params } from '../fn/book-transaction-history/borrow-book';
import { findAllBorrowedBooks } from '../fn/book-transaction-history/find-all-borrowed-books';
import { FindAllBorrowedBooks$Params } from '../fn/book-transaction-history/find-all-borrowed-books';
import { findAllReturnedBooks } from '../fn/book-transaction-history/find-all-returned-books';
import { FindAllReturnedBooks$Params } from '../fn/book-transaction-history/find-all-returned-books';
import { findByBookIdAndOwnerId } from '../fn/book-transaction-history/find-by-book-id-and-owner-id';
import { FindByBookIdAndOwnerId$Params } from '../fn/book-transaction-history/find-by-book-id-and-owner-id';
import { isBookBorrowed } from '../fn/book-transaction-history/is-book-borrowed';
import { IsBookBorrowed$Params } from '../fn/book-transaction-history/is-book-borrowed';
import { isBookBorrowedByUser } from '../fn/book-transaction-history/is-book-borrowed-by-user';
import { IsBookBorrowedByUser$Params } from '../fn/book-transaction-history/is-book-borrowed-by-user';
import { PageResponseBorrowedBookResponse } from '../models/page-response-borrowed-book-response';
import { returnBorrowBook } from '../fn/book-transaction-history/return-borrow-book';
import { ReturnBorrowBook$Params } from '../fn/book-transaction-history/return-borrow-book';


/**
 * Operations related to Book Transaction History
 */
@Injectable({ providedIn: 'root' })
export class BookTransactionHistoryService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `borrowBook()` */
  static readonly BorrowBookPath = '/transactions/borrow/{book-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `borrowBook()` instead.
   *
   * This method doesn't expect any request body.
   */
  borrowBook$Response(params: BorrowBook$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return borrowBook(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `borrowBook$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  borrowBook(params: BorrowBook$Params, context?: HttpContext): Observable<number> {
    return this.borrowBook$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `returnBorrowBook()` */
  static readonly ReturnBorrowBookPath = '/transactions/borrow/return/{book-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `returnBorrowBook()` instead.
   *
   * This method doesn't expect any request body.
   */
  returnBorrowBook$Response(params: ReturnBorrowBook$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return returnBorrowBook(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `returnBorrowBook$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  returnBorrowBook(params: ReturnBorrowBook$Params, context?: HttpContext): Observable<number> {
    return this.returnBorrowBook$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `approveReturnBorrowBook()` */
  static readonly ApproveReturnBorrowBookPath = '/transactions/borrow/return/approve/{book-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `approveReturnBorrowBook()` instead.
   *
   * This method doesn't expect any request body.
   */
  approveReturnBorrowBook$Response(params: ApproveReturnBorrowBook$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return approveReturnBorrowBook(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `approveReturnBorrowBook$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  approveReturnBorrowBook(params: ApproveReturnBorrowBook$Params, context?: HttpContext): Observable<number> {
    return this.approveReturnBorrowBook$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `isBookBorrowedByUser()` */
  static readonly IsBookBorrowedByUserPath = '/transactions/{bookId}/users/{userId}/is-borrowed';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `isBookBorrowedByUser()` instead.
   *
   * This method doesn't expect any request body.
   */
  isBookBorrowedByUser$Response(params: IsBookBorrowedByUser$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return isBookBorrowedByUser(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `isBookBorrowedByUser$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  isBookBorrowedByUser(params: IsBookBorrowedByUser$Params, context?: HttpContext): Observable<boolean> {
    return this.isBookBorrowedByUser$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
    );
  }

  /** Path part for operation `findByBookIdAndOwnerId()` */
  static readonly FindByBookIdAndOwnerIdPath = '/transactions/{bookId}/owner';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findByBookIdAndOwnerId()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByBookIdAndOwnerId$Response(params: FindByBookIdAndOwnerId$Params, context?: HttpContext): Observable<StrictHttpResponse<BookTransactionHistory>> {
    return findByBookIdAndOwnerId(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findByBookIdAndOwnerId$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findByBookIdAndOwnerId(params: FindByBookIdAndOwnerId$Params, context?: HttpContext): Observable<BookTransactionHistory> {
    return this.findByBookIdAndOwnerId$Response(params, context).pipe(
      map((r: StrictHttpResponse<BookTransactionHistory>): BookTransactionHistory => r.body)
    );
  }

  /** Path part for operation `isBookBorrowed()` */
  static readonly IsBookBorrowedPath = '/transactions/{bookId}/is-borrowed';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `isBookBorrowed()` instead.
   *
   * This method doesn't expect any request body.
   */
  isBookBorrowed$Response(params: IsBookBorrowed$Params, context?: HttpContext): Observable<StrictHttpResponse<boolean>> {
    return isBookBorrowed(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `isBookBorrowed$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  isBookBorrowed(params: IsBookBorrowed$Params, context?: HttpContext): Observable<boolean> {
    return this.isBookBorrowed$Response(params, context).pipe(
      map((r: StrictHttpResponse<boolean>): boolean => r.body)
    );
  }

  /** Path part for operation `findAllReturnedBooks()` */
  static readonly FindAllReturnedBooksPath = '/transactions/returned';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllReturnedBooks()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllReturnedBooks$Response(params?: FindAllReturnedBooks$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseBorrowedBookResponse>> {
    return findAllReturnedBooks(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllReturnedBooks$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllReturnedBooks(params?: FindAllReturnedBooks$Params, context?: HttpContext): Observable<PageResponseBorrowedBookResponse> {
    return this.findAllReturnedBooks$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseBorrowedBookResponse>): PageResponseBorrowedBookResponse => r.body)
    );
  }

  /** Path part for operation `findAllBorrowedBooks()` */
  static readonly FindAllBorrowedBooksPath = '/transactions/borrowed';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllBorrowedBooks()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBorrowedBooks$Response(params?: FindAllBorrowedBooks$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseBorrowedBookResponse>> {
    return findAllBorrowedBooks(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllBorrowedBooks$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBorrowedBooks(params?: FindAllBorrowedBooks$Params, context?: HttpContext): Observable<PageResponseBorrowedBookResponse> {
    return this.findAllBorrowedBooks$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseBorrowedBookResponse>): PageResponseBorrowedBookResponse => r.body)
    );
  }

}
