import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { ContextMenuService, DetailsViewService, NavigationPaneService, ToolbarService } from '@syncfusion/ej2-angular-filemanager';

/*
import {SERVER_API_URL} from "app/app.constants";
*/

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [NavigationPaneService, ToolbarService, DetailsViewService, ContextMenuService]
})
export class DocumentComponent implements OnInit {
  public ajaxSettings: object;
  public view: string;
  public hostUrl = SERVER_API_URL;

  public detailsViewSettings = {
    Columns: [
      {
        field: 'name',
        headerText: 'Nom',
        minWidth: 120,
        width: 'auto',
        customAttributes: { class: 'e-fe-grid-name' },
        template: '${name}'
      },
      { field: 'size', headerText: 'Taille', minWidth: 50, width: '110', template: '${size}' },

      {
        field: 'dateCreated',
        headerText: 'Date création',
        minWidth: 50,
        width: '190'
      },
      {
        field: 'dateModified',
        headerText: 'Date de modification',
        minWidth: 50,
        width: '190'
      }
    ]
  };

  constructor() {}

  public ngOnInit(): void {
    this.ajaxSettings = {
      url: this.hostUrl + 'fileManager'
      /* getImageUrl: this.hostUrl + 'api/FileManager/GetImage',
       uploadUrl: this.hostUrl + 'api/FileManager/Upload',
       downloadUrl: this.hostUrl + 'api/FileManager/Download'*/
    };
    this.view = 'Details';
  }
}
