$(document).ready(function() {
	adAnalysis(); // Load table data on document ready
});

function adAnalysis() {
	fetch("/rent_web/AdAnalysisServlet.do")
		.then((response) => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status} - ${response.statusText}`);
			}
			return response.json();
		})
		.then((data) => {
			const labels = Object.keys(data);
			const counts = Object.values(data);

			console.log("labels: " + labels + ", values: " + counts);

			const ctx2 = document.getElementById("adsChart").getContext("2d");
			new Chart(ctx2, {
				type: "pie",
				data: {
					labels: labels,
					datasets: [
						{
							label: "廣告刊登數量",
							data: counts,
							backgroundColor: [
								"rgba(255, 99, 132, 0.2)",
								"rgba(54, 162, 235, 0.2)",
								"rgba(255, 206, 86, 0.2)",
								"rgba(75, 192, 192, 0.2)",
							],
							borderColor: [
								"rgba(255, 99, 132, 1)",
								"rgba(54, 162, 235, 1)",
								"rgba(255, 206, 86, 1)",
								"rgba(75, 192, 192, 1)",
							],
							borderWidth: 1,
						},
					],
				},
				options: {
					responsive: true,
					plugins: {
						legend: { position: "top" },
						title: { display: true, text: "廣告刊登數據分析" },
					},
				},
			});
		})
		.catch((error) => {
		            console.error("Error fetching ad data:", error);
		        });
}

