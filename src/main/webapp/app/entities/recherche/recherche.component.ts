import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RechercheService } from 'app/entities/recherche/recherche.service';
import { Router } from '@angular/router';
import { FolderModel } from 'app/entities/recherche/FolderModel';
import { ChampModel } from 'app/entities/recherche/ChampModel';
import { ResultatComponent } from 'app/entities/resultat/resultat.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DocumentModel } from 'app/entities/recherche/DocumentModel';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

@Component({
  selector: 'jhi-entrepot',
  templateUrl: './recherche.component.html',
  styleUrls: ['./recherche.component.scss']
})
export class RechercheComponent implements OnInit {
  public isCollapsed = true;
  rech: FolderModel;
  public selectedMot0: any;
  public selectedMot: any;
  selectedMot1: any;
  selectedMot2: any;
  selectedMot3: any;
  selectedMot4: any;
  selectedMot5: any;
  folder: string;
  listfinal: any;
  fol: any;
  test: string;
  list = [];
  list2 = [];
  list3 = [];
  list22 = [];
  fin: DocumentModel[];
  champ = new ChampModel();

  users: DocumentModel[] = [];
  users1: DocumentModel[];
  error: any;
  success: any;
  links: any;
  totalItems: any;
  queryCount: any;
  page: any;
  headers: any;

  fil: string;
  constructor(
    private http: HttpClient,
    private rechercheService: RechercheService,
    private router: Router,
    private modalService: NgbModal,
    private alertService: JhiAlertService,
    private parseLinks: JhiParseLinks
  ) {}

  ngOnInit() {}

  BTNCancel() {
    this.router.navigateByUrl('/');
  }
  query(test) {
    this.fol = this.selectedMot0;
    this.rechercheService.rechercheQuery(this.fol).subscribe(resp => {
      this.list2 = resp;
    });
  }
  final() {
    this.router.navigate(['/rslt']);
    this.champ.m0 = this.selectedMot0;
    this.champ.m = this.selectedMot;
    this.champ.m1 = this.selectedMot1;
    this.champ.m2 = this.selectedMot2;
    this.champ.m3 = this.selectedMot3;
    this.champ.m4 = this.selectedMot4;
    this.champ.m5 = this.selectedMot5;
    this.rechercheService.rechercheFin(this.champ).subscribe(resp => {
      this.fin = resp;
    });
  }

  private onSuccess(data, headers) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.users1 = data;
    console.log('usersssss1', this.users1);
    console.log(this.users);
  }

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }

  // btnSavePressed() {
  //   const modalRef = this.modalService.open(ResultatComponent, {
  //     size: 'lg'
  //   });
  //   this.champ.m0 = this.selectedMot0;
  //   this.champ.m= this.selectedMot;
  //   this.champ.m1 = this.selectedMot1;
  //   this.champ.m2 = this.selectedMot2;
  //   this.champ.m3 = this.selectedMot3;
  //   this.champ.m4 = this.selectedMot4;
  //   this.champ.m5 = this.selectedMot5;
  //   this.rechercheService.rechercheFin(this.champ).subscribe(resp=>{
  //     this.fin=resp;
  //
  //   console.log('list fin'+ JSON.stringify(this.fin))
  //
  //   modalRef.componentInstance.vals= 1;
  //   console.log('ccccccc'+ modalRef.componentInstance.vals)
  //
  //   modalRef.result.then(
  //     result => {
  //       this.users.push(result.payload);
  //       // this.loadAll();
  //     },
  //     dissmissed => {
  //     }
  //   );
  //   })
  // }
  openDetail() {
    const modalRef1 = this.modalService.open(ResultatComponent, {
      size: 'lg',
      windowClass: 'my-class'
    });
    this.champ.m0 = this.selectedMot0;
    this.champ.m = this.selectedMot;
    this.champ.m1 = this.selectedMot1;
    this.champ.m2 = this.selectedMot2;
    this.champ.m3 = this.selectedMot3;
    this.champ.m4 = this.selectedMot4;
    this.champ.m5 = this.selectedMot5;

    console.log('selectedMot3  ' + this.selectedMot3);

    modalRef1.componentInstance.val0 = this.selectedMot0;
    modalRef1.componentInstance.val = this.selectedMot;
    modalRef1.componentInstance.val1 = this.selectedMot1;
    modalRef1.componentInstance.val2 = this.selectedMot2;
    modalRef1.componentInstance.val3 = this.selectedMot3;
    modalRef1.componentInstance.val4 = this.selectedMot4;
    modalRef1.componentInstance.val5 = this.selectedMot5;

    modalRef1.result.then(result => {});
  }

  // btnSave() {
  //   const modalRef = this.modalService.dismissAll();
  //   size: 'lg';
  // }
}
