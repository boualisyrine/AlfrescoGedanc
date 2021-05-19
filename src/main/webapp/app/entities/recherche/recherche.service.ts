import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINT } from 'app/app.constants';
import { HttpClient } from '@angular/common/http';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';
import { map } from 'rxjs/internal/operators';
import { ChampModel } from 'app/entities/recherche/ChampModel';

@Injectable({
  providedIn: 'root'
})
export class RechercheService {
  constructor(private http: HttpClient) {}

  getFoldPrinc(): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfolprinc');
  }

  fetchfoldern1(): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfoldern1');
  }

  fetchfilesn1(): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfilen1');
  }

  fetchfoldern2(mot: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/foldn2/' + mot);
  }

  fetchfilesn2(mot: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/filen2/' + mot, { responseType: 'json' });
  }

  fetchfoldern3(mot: string, mot2: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/foldn3/' + mot + '/' + mot2, { responseType: 'json' });
  }

  fetchfilesn3(mot: string, mot2: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/filen3/' + mot + '/' + mot2, { responseType: 'json' });
  }

  searchFolder(mot: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/searchFol/' + mot, { responseType: 'text' });
  }

  getDocument(mot: string, docs: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getdoc/' + mot + '/' + docs, { responseType: 'blob' });
  }

  downloadDoc(doc: DocumentModel[]): Observable<any> {
    return this.http
      .post(API_ENDPOINT + 'api/download', doc, {
        responseType: 'blob'
      })
      .pipe(map((response: any) => response));
  }
  getType(docs: DocumentModel[]): Observable<any> {
    return this.http.post(API_ENDPOINT + 'api/type', docs, { responseType: 'text' });
  }

  recherche(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/recherche/' + word, { responseType: 'json' });
  }
  recherchen2(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/recherchen2/' + word, { responseType: 'json' });
  }
  recherchen3(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/recherchen3/' + word, { responseType: 'json' });
  }
  searchDocuments(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/docs/' + word);
  }
  searchTypeDoc(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfiletype/' + word);
  }
  searchTypeDocn2(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfiletypen2/' + word);
  }

  searchDateDoc(deb: Date, fin: Date): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfileDate/' + deb + '/' + fin, { responseType: 'json' });
  }
  searchDateDocn2(deb: Date, fin: Date): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfileDaten2/' + deb + '/' + fin, { responseType: 'json' });
  }
  searchDesc(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfiledesc/' + word, { responseType: 'json' });
  }
  searchDescn2(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfiledescn2/' + word, { responseType: 'json' });
  }
  searchTitle(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfiletitle/' + word, { responseType: 'json' });
  }
  searchTitlen2(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfiletitlen2/' + word, { responseType: 'json' });
  }
  searchMot(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfilemot/' + word, { responseType: 'json' });
  }
  searchMotn2(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getfilemotn2/' + word, { responseType: 'json' });
  }
  recherchegeneral(): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/recherche/', { responseType: 'json' });
  }
  rechercheQuery(word: string): Observable<any> {
    return this.http.get(API_ENDPOINT + 'api/getmot/' + word, { responseType: 'json' });
  }
  rechercheFin(champ: ChampModel): Observable<any> {
    return this.http.post(API_ENDPOINT + 'api/recherchefin', champ, { responseType: 'json' });
  }
  download(doc: DocumentModel[]): Observable<any> {
    return this.http
      .post(API_ENDPOINT + 'api/downloadm2', doc, {
        responseType: 'blob'
      })
      .pipe(map((response: any) => response));
  }
  getTypem2(docs: DocumentModel[]): Observable<any> {
    return this.http.post(API_ENDPOINT + 'api/typem2', docs, { responseType: 'text' });
  }
}
