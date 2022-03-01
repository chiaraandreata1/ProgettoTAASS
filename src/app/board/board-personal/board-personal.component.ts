import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {Board} from "../../models/board";
import {FormControl} from "@angular/forms";
import {BoardService} from "../../services/board.service";

@Component({
  selector: 'app-board-personal',
  templateUrl: './board-personal.component.html',
  styleUrls: ['./board-personal.component.css']
})
export class BoardPersonalComponent implements OnInit {

  boards!: Observable<Board[]>;
  owner = new FormControl();
  sportBoard = new FormControl();

  constructor(private boardService: BoardService) { }

  ngOnInit(): void {
  }

  deleteBoard(id: number) {
    this.boardService.deleteBoard(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  reloadData() {
    this.boards = this.boardService.getBoardsListBySportAndOwner(this.sportBoard.value, this.owner.value);
  }
}
