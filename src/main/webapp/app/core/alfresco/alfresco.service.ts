import { Injectable } from '@angular/core';
import { flatMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from 'app/core/user/account.model';
@Injectable({ providedIn: 'root' })
export class AlfrescoService {
  constructor(private http: HttpClient) {}
  fetch(): Observable<any> {
    return this.http.get<any>(SERVER_API_URL + 'api/test');
  }

  fetch2(): Observable<any> {
    return this.http.get<any>(SERVER_API_URL + 'api/test');
  }

  fetchTree(): Observable<any> {
    return this.http.get<any>(SERVER_API_URL + 'api/folderTree');
  }
}
