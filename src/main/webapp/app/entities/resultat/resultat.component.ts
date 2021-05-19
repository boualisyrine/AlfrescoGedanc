import { Component, Input, OnInit } from '@angular/core';
import { ChampModel } from 'app/entities/recherche/ChampModel';
import { RechercheService } from 'app/entities/recherche/recherche.service';
import { Router } from '@angular/router';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-resultat',
  templateUrl: './resultat.component.html',
  styleUrls: ['./resultat.component.scss']
})
export class ResultatComponent implements OnInit {
  strin: string;
  account: Account;
  selectedMot: DocumentModel[];

  test: string;
  test3: string;
  types: string;
  types1: string;
  test2: string;
  khao: string;
  khao2: string;
  selectedfile: DocumentModel[];

  mot: string;
  list3 = [];
  lis: DocumentModel[];
  folder: string;
  file: string;
  @Input() val0;
  @Input() val;
  @Input() val1;
  @Input() val2;
  @Input() val3;
  @Input() val4;
  @Input() val5;
  champ = new ChampModel();
  fin: DocumentModel[];

  constructor(
    private rechercheService: RechercheService,
    private router: Router,
    private modalService: NgbModal,
    public activeModal: NgbActiveModal
  ) {}

  ngOnInit() {
    console.log('val ----- ', this.val0);
    console.log('val ----- ', this.val);
    console.log('val ----- ', this.val1);
    console.log('val ----- ', this.val2);
    console.log('val ----- ', this.val3);
    console.log('val ----- ', this.val4);
    console.log('val ----- ', this.val5);
    this.champ.m0 = this.val0;
    this.champ.m = this.val;
    this.champ.m1 = this.val1;
    this.champ.m2 = this.val2;
    this.champ.m3 = this.val3;
    this.champ.m4 = this.val4;
    this.champ.m5 = this.val5;
    this.rechercheService.rechercheFin(this.champ).subscribe(resp => {
      this.fin = resp;
      console.log('1111111111', JSON.stringify(this.fin));
    });
  }
  BTNCancel() {
    this.activeModal.close();
  }

  downloadFile(data: Blob, type: string) {
    this.strin = type;
    const blob = new Blob([data], { type: this.strin });
    const url = window.URL.createObjectURL(blob);
    console.log(url);
    window.open(url);
  }
  // downloadn2(fils) {
  //   this.khao = this.selectedMot;
  //   console.log('dos 2 ' + this.khao);
  //   this.selectedfile2[0].pathFolder = this.folderprinc + '/' + this.khao;
  //   this.lis = this.selectedfile2;
  //   this.rechercheService.getTypem2(this.lis).subscribe(resp => {
  //     // console.log('type' + JSON.stringify(resp));
  //     this.types = resp;
  //     console.log('111111111' + JSON.stringify(this.types));
  //   });
  //   console.log('22222222222' + JSON.stringify(this.types));
  //   this.rechercheService.downloadDoc(this.lis).subscribe(response => {
  //     console.log('download resppppppppp ' + response);
  //     this.downloadFile(response, this.types);
  //   });
  // }
  // downloadn3(fils) {
  //   this.khao = this.selectedMot;
  //   this.khao2 = this.selectedMot2;
  //   console.log('dos 2 ' + this.khao);
  //   this.selectedfile3[0].pathFolder = this.folderprinc + '/' + this.khao + '/' + this.khao2;
  //   this.lis = this.selectedfile3;
  //   this.rechercheService.getType(this.lis).subscribe(resp => {
  //     // console.log('type' + JSON.stringify(resp));
  //     this.types = resp;
  //     console.log('type n 3' + JSON.stringify(this.types));
  //   });
  //   console.log('resp ' + JSON.stringify(this.types));
  //   this.rechercheService.downloadDoc(this.lis).subscribe(response => {
  //     console.log('download n 3  ' + response);
  //     this.downloadFile(response, this.types);
  //   });
  // }

  download(fils) {
    // this.khao = this.selectedMot;
    this.lis = this.selectedMot;
    console.log('selectedfile ' + JSON.stringify(this.lis));

    this.rechercheService.getTypem2(this.lis).subscribe(resps => {
      // console.log('type' + JSON.stringify(resp));
      this.types1 = resps;
      console.log('type Mime***************' + JSON.stringify(this.types1));
    });
    this.rechercheService.download(this.lis).subscribe(responses => {
      console.log('download resppppppppp ' + responses);
      this.downloadFile(responses, this.types1);
    });
  }
}
