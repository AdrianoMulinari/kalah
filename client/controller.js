angular.module('KalahApp', [])
   .controller('KalahController', function($scope, $http) {
		
		$http.get("http://localhost:1111/start")
		.then(function(response) {
			$scope.content = response.data;
			$scope.statuscode = response.status;
			$scope.statustext = response.statustext;
			
			$scope.matrix=[[
			$scope.content.boardRows[0].pits[0],
			$scope.content.boardRows[0].pits[1],
			$scope.content.boardRows[0].pits[2],
			$scope.content.boardRows[0].pits[3],
			$scope.content.boardRows[0].pits[4],
			$scope.content.boardRows[0].pits[5]]
			,[
			$scope.content.boardRows[1].pits[0],
			$scope.content.boardRows[1].pits[1],
			$scope.content.boardRows[1].pits[2],
			$scope.content.boardRows[1].pits[3],
			$scope.content.boardRows[1].pits[4],
			$scope.content.boardRows[1].pits[5]]
			,[
			$scope.content.boardRows[2].pits[0],
			$scope.content.boardRows[2].pits[1],
			$scope.content.boardRows[2].pits[2],
			$scope.content.boardRows[2].pits[3],
			$scope.content.boardRows[2].pits[4],
			$scope.content.boardRows[2].pits[5]]];
		});	
		
		$scope.readOnly = function(index, row, rowIndex)  {
			
			if (row[index]==0) {
				return true;
			}
			
			if (rowIndex==1) {
				return true;
			}
			
			if ($scope.content.player == "TWO" && rowIndex==0) {
				return true;
			}
			
			if ($scope.content.player == "ONE" && rowIndex==2) {
				return true;
			}
			
		};
		
		$scope.move = function(index) {
			
			$http.get("http://localhost:1111/move", {params: {"pit": index, "boardId": $scope.content.id}})
			.then(function(response) {
				$scope.content = response.data;
				$scope.statuscode = response.status;
				$scope.statustext = response.statustext;
				
				$scope.matrix=[[
				$scope.content.boardRows[0].pits[0],
				$scope.content.boardRows[0].pits[1],
				$scope.content.boardRows[0].pits[2],
				$scope.content.boardRows[0].pits[3],
				$scope.content.boardRows[0].pits[4],
				$scope.content.boardRows[0].pits[5]]
				,[
				$scope.content.boardRows[1].pits[0],
				$scope.content.boardRows[1].pits[1],
				$scope.content.boardRows[1].pits[2],
				$scope.content.boardRows[1].pits[3],
				$scope.content.boardRows[1].pits[4],
				$scope.content.boardRows[1].pits[5]]
				,[
				$scope.content.boardRows[2].pits[0],
				$scope.content.boardRows[2].pits[1],
				$scope.content.boardRows[2].pits[2],
				$scope.content.boardRows[2].pits[3],
				$scope.content.boardRows[2].pits[4],
				$scope.content.boardRows[2].pits[5]]];
			});	
		};
});

		