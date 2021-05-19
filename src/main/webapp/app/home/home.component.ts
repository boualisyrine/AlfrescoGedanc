import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

import { HttpClient } from '@angular/common/http';
import { RechercheService } from 'app/entities/recherche/recherche.service';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';
@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  strin: string;
  account: Account;
  authSubscription: Subscription;
  modalRef: NgbModalRef;
  listFold = [];
  listFil = [];
  test: string;
  test3: string;
  types: string;
  types1: string;
  test2: string;
  khao: string;
  khao2: string;
  selectedfile: DocumentModel[];
  selectedMot: string;
  selectedfile3: DocumentModel[];
  selectedMot3: string;
  selectedfile2: DocumentModel[];
  selectedMot2: string;
  mot: string;
  folderprinc: string;
  list2 = [];
  list3 = [];
  listn3 = [];
  listn2 = [];
  lis: DocumentModel[];
  fils: DocumentModel;
  folder: string;
  folder3: string;
  file: string;
  dep = 3;
  fold: string;
  public isCollap = true;
  public isColl = true;
  public isCollaps = true;
  public isCollapse = true;
  public isCollapsed = true;
  del: string;
  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private http: HttpClient,
    private rechercheService: RechercheService
  ) {}
  ngOnInit() {
    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
    this.rechercheService.getFoldPrinc().subscribe(
      resp => {
        this.folderprinc = resp;
      },
      error => {},
      () => {}
    );
    this.rechercheService.fetchfoldern1().subscribe(
      response => {
        this.listFold = response;
      },
      error => {},
      () => {}
    );
    this.rechercheService.fetchfilesn1().subscribe(
      response => {
        this.listFil = response;
        console.log('niveau 1 files ' + JSON.stringify(response));
      },
      error => {},
      () => {}
    );
  }
  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', () => {
      this.accountService.identity().subscribe(account => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }
  foldern2(folder) {
    this.test = this.selectedMot;

    this.rechercheService.fetchfoldern2(this.test).subscribe(resps => {
      console.log('fetchfoldern2 ' + JSON.stringify(resps));

      this.list2 = resps;
    });
    this.rechercheService.fetchfilesn2(this.test).subscribe(reponse => {
      console.log('list33333333 ' + JSON.stringify(reponse));
      this.list3 = reponse;
      // for (i = 0; i < this.list3.length; i++) {
      //   if (this.list3[i] === this.selectedMot2) {
      //     console.log('this.list3[i] ' + this.list3[i]);
      //     console.log('selectedMot2 ' + this.selectedMot2);
      //     this.list3[i].pathFolder = this.folderprinc + '/' + this.selectedMot2;
      //   }
      // }
      console.log('file object test path' + JSON.stringify(this.list3));
    });
  }

  foldern3(folder, folder3) {
    this.test = this.selectedMot.toString();
    this.test3 = this.selectedMot2.toString();
    console.log('this.test ' + this.test);
    console.log('this.test3 ' + this.test3);
    this.rechercheService.fetchfoldern3(this.test, this.test3).subscribe(respd => {
      console.log('folder n3 ' + JSON.stringify(respd));
      this.listn2 = respd;
      console.log('folder n3 ' + JSON.stringify(this.listn2));
    });
    this.rechercheService.fetchfilesn3(this.test, this.test3).subscribe(reponse => {
      this.listn3 = reponse;
      console.log('file  n3' + JSON.stringify(this.listn3));
    });
  }
  login() {
    this.modalRef = this.loginModalService.open();
  }
  ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }
  downloadFile(data: Blob, type: string) {
    this.strin = type;
    const blob = new Blob([data], { type: this.strin });
    const url = window.URL.createObjectURL(blob);
    console.log(url);
    window.open(url);
  }
  downloadn2(fils) {
    this.khao = this.selectedMot;
    console.log('dos 2 ' + this.khao);
    this.selectedfile2[0].pathFolder = this.folderprinc + '/' + this.khao;
    this.lis = this.selectedfile2;
    this.rechercheService.getType(this.lis).subscribe(resp => {
      // console.log('type' + JSON.stringify(resp));
      this.types = resp;
      console.log('111111111' + JSON.stringify(this.types));
    });
    console.log('22222222222' + JSON.stringify(this.types));
    this.rechercheService.downloadDoc(this.lis).subscribe(response => {
      console.log('download resppppppppp ' + response);
      this.downloadFile(response, this.types);
    });
  }
  downloadn3(fils) {
    this.khao = this.selectedMot;
    this.khao2 = this.selectedMot2;
    console.log('dos 2 ' + this.khao);
    this.selectedfile3[0].pathFolder = this.folderprinc + '/' + this.khao + '/' + this.khao2;
    this.lis = this.selectedfile3;
    this.rechercheService.getType(this.lis).subscribe(resp => {
      // console.log('type' + JSON.stringify(resp));
      this.types = resp;
      console.log('type n 3' + JSON.stringify(this.types));
    });
    console.log('resp ' + JSON.stringify(this.types));
    this.rechercheService.downloadDoc(this.lis).subscribe(response => {
      console.log('download n 3  ' + response);
      this.downloadFile(response, this.types);
    });
  }

  downloadn1(fils) {
    // this.khao = this.selectedMot;
    console.log(' dossier principal' + this.folderprinc);
    this.selectedfile[0].pathFolder = this.folderprinc.toString();
    console.log('path file' + this.selectedfile[0].pathFolder);
    this.lis = this.selectedfile;
    console.log('selectedfile ' + JSON.stringify(this.lis));

    this.rechercheService.getType(this.lis).subscribe(resps => {
      // console.log('type' + JSON.stringify(resp));
      this.types1 = resps;
      console.log('type Mime***************' + JSON.stringify(this.types1));
    });
    console.log('22222222222' + JSON.stringify(this.types1));
    this.rechercheService.downloadDoc(this.lis).subscribe(responses => {
      console.log('download resppppppppp ' + responses);
      this.downloadFile(responses, this.types1);
    });
  }
}
