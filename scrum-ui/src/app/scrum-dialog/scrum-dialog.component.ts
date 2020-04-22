import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ScrumService } from '../service/scrum-service.service';
import { Scrum } from '../model/scrum/scrum';

@Component({
  selector: 'app-scrum-dialog',
  templateUrl: './scrum-dialog.component.html',
  styleUrls: ['./scrum-dialog.component.css']
})
export class ScrumDialogComponent implements OnInit {

  title : string;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ScrumDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private scrumService: ScrumService) {

      this.form = fb.group({
        title: [this.title, Validators.required]
    });
    }

  ngOnInit() {
  }

  close() {
    this.dialogRef.close();
  }

  save() {
    this.title = this.form.get('title').value;
    if (this.title) {
      this.scrumService.saveNewScrum(this.title).subscribe(

        response => {
          console.log(response)
        }
      )
    }
    this.dialogRef.close();
  }

}
