import { Component, OnInit } from '@angular/core';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { RechercheService } from 'app/entities/recherche/recherche.service';

@Component({
  selector: 'jhi-mot',
  templateUrl: './mot.component.html',
  styleUrls: ['./mot.component.scss']
})
export class MotComponent implements OnInit {
  test: any;
  list3 = [];
  list4 = [];
  listtot = [];
  fol: string;
  selectedMot0: string;
  selectedfile: DocumentModel[];
  strin: string;
  folderprinc: string;
  types1: string;
  lis: DocumentModel[];
  types: string;

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
    private rechercheService: RechercheService
  ) {}

  ngOnInit() {
    this.rechercheService.getFoldPrinc().subscribe(
      resp => {
        this.folderprinc = resp;
      },
      error => {},
      () => {}
    );
    this.route.params.subscribe(params => {
      this.test = params.id; // --> Name must match wanted parameter
    });
    this.fol = this.test;
    this.rechercheService.searchMot(this.fol).subscribe(resp => {
      this.list3 = resp;
      console.log(' mot clé ' + JSON.stringify(this.list3));
    });
    this.fol = this.test;
    this.rechercheService.searchMotn2(this.fol).subscribe(resp => {
      this.list4 = resp;
      console.log(' list22 ' + JSON.stringify(this.list4));
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
      this.types1 = resps;
    });
    this.rechercheService.downloadDoc(this.lis).subscribe(responses => {
      this.downloadFile(responses, this.types1);
    });
  }

  downloadn2(fils) {
    this.lis = this.selectedfile;
    this.rechercheService.getType(this.lis).subscribe(resp => {
      this.types = resp;
    });
    this.rechercheService.downloadDoc(this.lis).subscribe(response => {
      this.downloadFile(response, this.types);
    });
  }
  BTNCancel() {
    this.router.navigateByUrl('/');
  }
}
