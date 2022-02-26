import { Component, OnInit } from '@angular/core';
import {Board} from "../board";
import {BoardService} from "../board.service";

@Component({
  selector: 'app-create-board',
  templateUrl: './create-board.component.html',
  styleUrls: ['./create-board.component.css']
})
export class CreateBoardComponent implements OnInit {

  board: Board = new Board();
  submitted = false;

  constructor(private boardService: BoardService) { }

  ngOnInit(): void {
  }

  newBoard(): void{
    this.submitted = false;
    this.board = new Board();
  }

  save() {
    this.boardService.createBoard(this.board)
      .subscribe(data => console.log(data), error => console.log(error));
    this.board = new Board();
  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }
}
