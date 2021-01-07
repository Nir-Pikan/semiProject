package mocks;

import entities.Permission;
import entities.Permissions;
import entities.Worker;
import logic.WorkerController;

/**
 * a one use class for easy creation of workers to DB
 */
public class WorkerRegistrationSystem {

	public WorkerRegistrationSystem() {
	}

	/**
	 * adds workers to DB
	 * 
	 * @param workerController GoNature's worker controller
	 */
	public static void createWorkers(WorkerController workerController) {
		// ================ Create Permissions for workers ================
		Permissions depManager = new Permissions("department");
		Permissions parkSilverManager = new Permissions("Silver");
		Permissions parkSilverWorker = new Permissions("Silver");
		Permissions parkGoldManager = new Permissions("Gold");
		Permissions parkGoldWorker = new Permissions("Gold");
		Permissions parkPlatinumManager = new Permissions("Platinum");
		Permissions parkPlatinumWorker = new Permissions("Platinum");

		Permission reg = new Permission("Registration");
		Permission visitorsView = new Permission("VistitorsView");
		Permission reportExport = new Permission("ReportExport");
		Permission editParameters = new Permission("EditParameters");
		Permission addDiscounts = new Permission("AddDiscounts");

		depManager.AddPermission(reg);
		depManager.AddPermission(visitorsView);
		depManager.AddPermission(reportExport);
		depManager.AddPermission(new Permission("ApproveParameters"));
		depManager.AddPermission(new Permission("ApproveDiscounts"));

		parkSilverManager.AddPermission(reg);
		parkSilverManager.AddPermission(visitorsView);
		parkSilverManager.AddPermission(reportExport);
		parkSilverManager.AddPermission(editParameters);
		parkSilverManager.AddPermission(addDiscounts);

		parkGoldManager.AddPermission(reg);
		parkGoldManager.AddPermission(visitorsView);
		parkGoldManager.AddPermission(reportExport);
		parkGoldManager.AddPermission(editParameters);
		parkGoldManager.AddPermission(addDiscounts);

		parkPlatinumManager.AddPermission(reg);
		parkPlatinumManager.AddPermission(visitorsView);
		parkPlatinumManager.AddPermission(reportExport);
		parkPlatinumManager.AddPermission(editParameters);
		parkPlatinumManager.AddPermission(addDiscounts);

		parkSilverWorker.AddPermission(reg);
		parkSilverWorker.AddPermission(visitorsView);

		parkGoldWorker.AddPermission(reg);
		parkGoldWorker.AddPermission(visitorsView);

		parkPlatinumWorker.AddPermission(reg);
		parkPlatinumWorker.AddPermission(visitorsView);
		// =================================================================================================================

		// ================ Create Workers for DB ================
		Worker departmentManager = new Worker("depManager", "Nir", "Pikan", "000000001", "nir@gmail.com",
				"departmentManager", "Aa123456", false, depManager);
		Worker silverManager = new Worker("silverManager", "Roman", "Kozak", "000000002", "roman@gmail.com",
				"parkManager", "Aa123456", false, parkSilverManager);
		Worker goldManager = new Worker("goldManager", "Or", "Man", "000000003", "or@gmail.com", "parkManager",
				"Aa123456", false, parkGoldManager);
		Worker platinumManager = new Worker("platinumManager", "Michael", "Gindin", "000000004", "michael@gmail.com",
				"parkManager", "Aa123456", false, parkPlatinumManager);
		Worker silverWorker = new Worker("silverWorker", "Aviv", "Vanunu", "000000005", "aviv@gmail.com", "parkWorker",
				"Aa123456", false, parkSilverWorker);
		Worker goldWorker = new Worker("goldWorker", "Giorno", "Giovanna", "000000006", "giorno@gmail.com",
				"parkWorker", "Aa123456", false, parkGoldWorker);
		Worker platinumWorker = new Worker("platinumWorker", "Jotaro", "Kujo", "000000007", "jotaro@gmail.com",
				"parkWorker", "Aa123456", false, parkPlatinumWorker);

		workerController.AddWorker(departmentManager);
		workerController.AddWorker(silverManager);
		workerController.AddWorker(goldManager);
		workerController.AddWorker(platinumManager);
		workerController.AddWorker(silverWorker);
		workerController.AddWorker(goldWorker);
		workerController.AddWorker(platinumWorker);
		// =================================================================================================================

	}

}
