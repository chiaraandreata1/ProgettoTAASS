import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Board} from "../../models/board";
import {BoardService} from "../../services/board.service";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-boards-list',
  templateUrl: './boards-list.component.html',
  styleUrls: ['./boards-list.component.css']
})
export class BoardsListComponent implements OnInit {

  boards!: Observable<Board[]>;
  sportBoard = new FormControl();
  typeBoard = new FormControl();
  dateBoard = new FormControl();

  maxDate: Date;

  constructor(private boardService: BoardService) {
    this.maxDate = new Date();
  }

  ngOnInit(): void {
    //this.reloadData();
  }

  deleteBoards() {
    this.boardService.deleteAllBoards()
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log('ERROR: ' + error));
  }

  reloadData() {
    console.log(this.dateBoard.value);
    if (this.dateBoard.value==null)
      this.boards = this.boardService.getBoardsListBySportAndType(this.sportBoard.value, this.typeBoard.value);
    else
      this.boards = this.boardService.getBoardsListBySportAndTypeAndDate(this.sportBoard.value, this.typeBoard.value, new Date(this.dateBoard.value.toString()).toISOString().split('T')[0]);
  }
}
