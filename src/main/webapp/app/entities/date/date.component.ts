import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { RechercheService } from 'app/entities/recherche/recherche.service';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';

@Component({
  selector: 'jhi-date',
  templateUrl: './date.component.html',
  styleUrls: ['./date.component.scss']
})
export class DateComponent implements OnInit {
  test2: Date;
  test1: Date;
  fol1: Date;
  fol2: Date;
  list4 = [];
  list5 = [];
  selectedfile: DocumentModel[];
  strin: string;
  folderprinc: string;
  types1: string;
  lis: DocumentModel[];
  msg: string;
  constructor(
    private modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
    private rechercheService: RechercheService
  ) {}
  ngOnInit() {
    this.route.params.subscribe(params => {
      this.test1 = params.id1; // --> Name must match wanted parameter
      this.test2 = params.id2; // --> Name must match wanted parameter
    });
    this.rechercheService.getFoldPrinc().subscribe(
      resp => {
        this.folderprinc = resp;
      },
      error => {},
      () => {}
    );

    this.fol1 = this.test1;
    this.fol2 = this.test2;
    this.rechercheService.searchDateDoc(this.fol1, this.fol2).subscribe(resps => {
      this.list4 = resps;
      console.log('doc date ' + JSON.stringify(this.list4));
    });
    this.rechercheService.searchDateDocn2(this.fol1, this.fol2).subscribe(resps => {
      this.list5 = resps;
    });
  }
  downloadFile(data: Blob, type: string) {
    this.strin = type;
    const blob = new Blob([data], { type: this.strin });
    const url = window.URL.createObjectURL(blob);
    console.log(url);
    window.open(url);
  }
  downloadn1(fils) {
    // this.khao = this.selectedMot;
    this.selectedfile[0].pathFolder = this.folderprinc.toString();
    this.lis = this.selectedfile;
    this.rechercheService.getType(this.lis).subscribe(resps => {
      // console.log('type' + JSON.stringify(resp));
      this.types1 = resps;
    });
    this.rechercheService.downloadDoc(this.lis).subscribe(responses => {
      this.downloadFile(responses, this.types1);
    });
  }
  downloadn2(fils) {
    this.lis = this.selectedfile;
    this.rechercheService.getType(this.lis).subscribe(resp => {
      this.types1 = resp;
    });
    this.rechercheService.downloadDoc(this.lis).subscribe(response => {
      this.downloadFile(response, this.types1);
    });
  }
  BTNCancel() {
    this.router.navigateByUrl('/');
  }
}
