import { Component, OnInit } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

import { JhiHealthService } from './health.service';
import { JhiHealthModalComponent } from './health-modal.component';
import { AlfrescoService } from 'app/core/alfresco/alfresco.service';

@Component({
  selector: 'jhi-health',
  templateUrl: './health.component.html'
})
export class JhiHealthCheckComponent implements OnInit {
  healthData: any;
  updatingHealth: boolean;
  closeResult = '';

  constructor(private modalService: NgbModal, private healthService: JhiHealthService, private alfrescoService: AlfrescoService) {}

  ngOnInit() {
    console.log('**************************************************************************');
    this.refresh();
    this.alfrescoService.fetch2().subscribe(
      res => {
        console.log('*******************************************+++++++++++++++++*******************************');
        console.log(res);
      },
      ex => console.log(ex)
    );
  }

  baseName(name: string) {
    return this.healthService.getBaseName(name);
  }

  getBadgeClass(statusState) {
    if (statusState === 'UP') {
      return 'badge-success';
    } else {
      return 'badge-danger';
    }
  }

  refresh() {
    this.updatingHealth = true;

    this.healthService.checkHealth().subscribe(
      health => {
        this.healthData = this.healthService.transformHealthData(health);
        this.updatingHealth = false;
      },
      error => {
        if (error.status === 503) {
          this.healthData = this.healthService.transformHealthData(error.error);
          this.updatingHealth = false;
        }
      }
    );
  }

  showHealth(health: any) {
    const modalRef = this.modalService.open(JhiHealthModalComponent);
    modalRef.componentInstance.currentHealth = health;
  }

  subSystemName(name: string) {
    return this.healthService.getSubSystemName(name);
  }

  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      result => {
        this.closeResult = `Closed with: ${result}`;
      },
      reason => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }
}
