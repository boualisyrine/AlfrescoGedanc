import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { RechercheService } from 'app/entities/recherche/recherche.service';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';
import { FolderModel } from 'app/entities/recherche/FolderModel';

@Component({
  selector: 'jhi-file',
  templateUrl: './file.component.html',
  styleUrls: ['./file.component.scss']
})
export class FileComponent implements OnInit {
  test: any;
  strin: string;
  khao: string;
  types: string;
  lis: any;
  folderprinc: string;
  list3 = [];
  selectedfile: string;
  rech: FolderModel;
  selectedMot: string;
  folder: string;
  fol: string;
  list0: DocumentModel[];
  list: DocumentModel[];
  listn2: DocumentModel[];
  listn3: DocumentModel[];

  fil: string;
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
    this.rechercheService.getFoldPrinc().subscribe(
      resp => {
        this.folderprinc = resp;
      },
      error => {},
      () => {}
    );
    this.fil = this.test;
    this.rechercheService.recherche(this.fil).subscribe(resps => {
      this.list = resps;
      console.log('new test ' + JSON.stringify(this.list));
    });
    this.rechercheService.recherchen2(this.fil).subscribe(resps => {
      this.listn2 = resps;
      console.log('new test n2 ' + JSON.stringify(this.listn2));
    });

    this.rechercheService.recherchen3(this.fil).subscribe(resps => {
      this.listn3 = resps;
    });
  }

  downloadFile(data: Blob, type: string) {
    this.strin = type;
    this.strin = type;
    const blob = new Blob([data], { type: this.strin });
    const url = window.URL.createObjectURL(blob);
    console.log(url);
    window.open(url);
  }
  recherche(test) {
    this.rechercheService.getType(this.list).subscribe(resp => {
      this.types = resp;
    });
    console.log('22222222222' + this.types);
    this.rechercheService.downloadDoc(this.list).subscribe(response => {
      this.downloadFile(response, this.types);
    });
  }
  recherchen2(test) {
    console.log(' objet ' + JSON.stringify(this.listn2));
    this.rechercheService.getType(this.listn2).subscribe(resp => {
      this.types = resp;
    });
    this.rechercheService.downloadDoc(this.listn2).subscribe(response => {
      this.downloadFile(response, this.types);
    });
  }
  recherchen3(test) {
    this.rechercheService.getType(this.listn2).subscribe(resp => {
      this.types = resp;
    });
    this.rechercheService.downloadDoc(this.listn3).subscribe(response => {
      this.downloadFile(response, this.types);
    });
  }
  BTNCancel() {
    this.router.navigateByUrl('/');
  }
}
