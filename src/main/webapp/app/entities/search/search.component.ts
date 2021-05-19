import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { RechercheService } from 'app/entities/recherche/recherche.service';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  strin: string;
  test: any;
  khao: string;
  types: string;
  lis: DocumentModel[];
  folderprinc: string;
  list3 = [];
  public isCollap = true;
  selectedfile2: DocumentModel[];

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
    private rechercheService: RechercheService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.test = params.id; // --> Name must match wanted parameter
    });

    this.rechercheService.fetchfilesn2(this.test).subscribe(reponse => {
      this.list3 = reponse;
    });

    this.rechercheService.getFoldPrinc().subscribe(
      resp => {
        this.folderprinc = resp;
      },
      error => {},
      () => {}
    );
  }
  downloadFile(data: Blob, type: string) {
    this.strin = type;
    const blob = new Blob([data], { type: this.strin });
    const url = window.URL.createObjectURL(blob);
    console.log(url);
    window.open(url);
  }
  downloadn2(fils) {
    this.khao = this.test;
    this.selectedfile2[0].pathFolder = this.folderprinc + '/' + this.khao;
    this.lis = this.selectedfile2;
    this.rechercheService.getType(this.lis).subscribe(resp => {
      // console.log('type' + JSON.stringify(resp));
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
