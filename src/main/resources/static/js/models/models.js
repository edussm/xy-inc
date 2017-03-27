angular.module('models', []).controller('models', function ($scope, $http, $location) {
	$http.get('/user/').then(function (response) {
		$scope.user = response.data.name;
	});
	$http.get('/api/v1/modelDefinition').then(function (response) {
		$scope.itens = response.data;
	});
	$scope.cols = {};
	$scope.addModelDefinition = function (isValid) {
		if (isValid) {
			var modelDefinition = {
				'modelName': $scope.modelName,
				'columns': $scope.cols
			};

			console.log(modelDefinition);

			var res = $http.post('/api/v1/modelDefinition', modelDefinition);
			res.success(function (data, status, headers, config) {
				$scope.itens.push(data);
				$scope.modelName = "";
				$scope.cols = {};
				$location.path( "/" );
			});
			res.error(function (data, status, headers, config) {
				self.error = true;
				self.errorMessage = "Falha ao salvar o Modelo: " + data.error
					+ "(" + data.message + ")";
			});
		};
	};

	$scope.deleteModel = function (item, index) {
		var res = $http.delete('/api/v1/modelDefinition/' + item.modelName);
		res.success(function (data, status, headers, config) {
			$scope.itens.splice(index, 1);
		});
		res.error(function (data, status, headers, config) {
			self.error = true;
			self.errorMessage = "Falha ao apagar o Modelo: "
				+ data.error
				+ "(" + data.message + ")";
		});
	};

	function validate(str) {
		return !(!str || /^\s*$/.test(str) || 0 === str.length);
	}

	$scope.addColumnToForm = function (col) {
		console.log(col);
		console.log(validate(col.name));
		if (validate(col.name) && validate(col.type)) {
			$scope.cols[col.name] = col.type;
			col.name = "";
			console.log($scope.colsy);
		}
	};

	$scope.deleteColumnForm = function (key) {
		console.log(key);
		delete $scope.cols[key];
		console.log(key);
		console.log($scope.cols);

	};

});
