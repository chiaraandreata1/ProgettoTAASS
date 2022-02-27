import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Board} from "../board";
import {BoardService} from "../board.service";
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

  constructor(private boardService: BoardService) { }

  ngOnInit(): void {
    //this.reloadData();
  }

  deleteBoards(sport: string, type: string) {
    this.boardService.deleteAllBoards()
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log('ERROR: ' + error));
  }

  reloadData() {
    this.boards = this.boardService.getBoardsListBySportAndType(this.sportBoard.value, this.typeBoard.value);
  }
}
