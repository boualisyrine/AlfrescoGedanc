import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { ContextMenuService, DetailsViewService, NavigationPaneService, ToolbarService } from '@syncfusion/ej2-angular-filemanager';
import { L10n } from '@syncfusion/ej2-base';

/*
import {SERVER_API_URL} from "app/app.constants";
*/

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [NavigationPaneService, ToolbarService, DetailsViewService, ContextMenuService, DetailsViewService]
})
export class DocumentComponent implements OnInit {
  public ajaxSettings: object;
  public view: string;
  public hostUrl = SERVER_API_URL;
  public locale: string;
  toolbarSettings = {
    items: [
      'NewFolder',
      'Upload',
      'Cut',
      'Copy',
      'Paste',
      'Delete',
      'Download',
      'Rename',
      'SortBy',
      'Refresh',
      'Selection',
      'View',
      'Details'
    ],
    visible: true
  };

  detailsViewSettings = {
    columns: [
      {
        field: 'name',
        headerText: 'Nom',
        minWidth: 120,
        width: 'auto',
        customAttributes: { class: 'e-fe-grid-name' },
        template: '${name}'
      },
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
    L10n.load({
      fr: {
        filemanager: {
          NewFolder: 'Nouveau Dossier',
          Upload: 'Chargement',
          Delete: 'Supprimer',
          Rename: 'Renommer',
          Download: 'Télécharger',
          Cut: 'Couper',
          Copy: 'Copier',
          Paste: 'Coller',
          SortBy: 'Trier par',
          Refresh: 'Actualiser',
          'Item-Selection': 'Item sélectionnée',
          'Items-Selection': 'Item sélectionnées',
          View: 'Voir',
          Details: 'Détails',
          SelectAll: 'Tout sélectionner',
          Open: 'Ouvrir',
          'Tooltip-NewFolder': 'Nouveau dossier',
          'Tooltip-Upload': 'Chargement',
          'Tooltip-Delete': 'Supprimer',
          'Tooltip-Rename': 'Renommer',
          'Tooltip-Download': 'Télécharger',
          'Tooltip-Cut': 'Couper',
          'Tooltip-Copy': 'Copier',
          'Tooltip-Paste': 'Coller',
          'Tooltip-SortBy': 'Trier par',
          'Tooltip-Refresh': 'Actualiser',
          'Tooltip-Selection': 'Sélectionner',
          'Tooltip-View': 'Voir',
          'Tooltip-Details': 'Détails',
          'Tooltip-SelectAll': 'Tout sélectionner',
          Name: 'Nom',
          Size: 'Taille',
          DateModified: 'Date de modification',
          DateCreated: 'Date de création',
          Path: 'Chemin',
          Modified: 'Modifiée',
          Created: 'Crée',
          Location: 'Emplacement',
          Type: 'Type',
          Permission: 'Permission',
          Ascending: 'Ascendant',
          Descending: 'Descendant',
          None: 'Rien',
          'View-LargeIcons': 'Grandes icônes',
          'View-Details': 'Détails',
          Search: 'Chercher',
          'Button-Ok': 'OK',
          'Button-Save': 'Sauvgarder',
          'Button-Yes': 'Oui',
          'Button-No': 'Non',
          'Button-Cancel': 'Annuler',
          'Header-NewFolder': 'Dossier',
          'Content-NewFolder': 'Entrez votre nom de dossier',
          'Button-Create': 'Créer',
          'Validation-Empty': 'Le nom du fichier ou du dossier ne peut pas être vide.',
          'Header-Folder-Delete': 'Supprimer dossier',
          'Content-Folder-Delete': 'Êtes-vous sûr de vouloir supprimer ce dossier ?',
          'Header-Delete': 'Supprimer fichier',
          'Content-Delete': 'Êtes-vous sûr de vouloir supprimer ce fichier ?',
          'Header-Multiple-Delete': 'Supprimer plusieurs fichiers',
          'Content-Multiple-Delete': 'Voulez-vous vraiment supprimer ces {0} fichiers ?',
          'Validation-Invalid':
            'Le nom de fichier ou de dossier {0} contient des caractères non valides. Veuillez utiliser un nom différent. Les noms de fichiers ou de dossiers valides ne peuvent pas se terminer par un point ou un espace et ne peuvent contenir aucun des caractères suivants : \\ /: *? ”<> |',
          'Validation-Rename-Exists': 'Impossible de renommer {0} en {1}',
          'Header-Rename': 'Renommer',
          'Content-Rename': 'Entrez votre nouveau nom',
          'Header-Rename-Confirmation': 'Confirmation',
          'Content-Rename-Confirmation':
            'Si vous modifiez une extension de nom de fichier, le fichier peut devenir instable. Voulez-vous vraiment les changer?'
        }
      }
    });

    this.locale = 'fr';
    this.ajaxSettings = {
      url: this.hostUrl + 'fileManager',
      downloadUrl: this.hostUrl + 'fileManager/download'
      /* getImageUrl: this.hostUrl + 'api/FileManager/GetImage',
       uploadUrl: this.hostUrl + 'api/FileManager/Upload',
       downloadUrl: this.hostUrl + 'api/FileManager/Download'*/
    };
    this.view = 'Details';
  }
}
