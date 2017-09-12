package com.coding.challenge.kalah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KalahRest {

	@Autowired
	private KalahService kalahService;

	@RequestMapping("/move")
	public Board move(@RequestParam(value="pit") int pit, @RequestParam(value="boardId") long boardId) {
		return kalahService.moveSeed(pit, boardId);
	}
	
	@RequestMapping("/start")
	public Board start() {
		return kalahService.createNewBoard();
	}
}
