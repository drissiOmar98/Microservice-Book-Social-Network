/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { BookTransactionHistory } from '../../models/book-transaction-history';

export interface FindByBookIdAndOwnerId$Params {
  bookId: number;
}

export function findByBookIdAndOwnerId(http: HttpClient, rootUrl: string, params: FindByBookIdAndOwnerId$Params, context?: HttpContext): Observable<StrictHttpResponse<BookTransactionHistory>> {
  const rb = new RequestBuilder(rootUrl, findByBookIdAndOwnerId.PATH, 'get');
  if (params) {
    rb.path('bookId', params.bookId, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<BookTransactionHistory>;
    })
  );
}

findByBookIdAndOwnerId.PATH = '/transactions/{bookId}/owner';
