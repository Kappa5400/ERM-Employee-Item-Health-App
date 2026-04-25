
import { EmployeeAPI, ItemAPI, BossAPI } from './api-service.js';
import { restrictDateInputs, populateBossDropdown, handleApiError } from './ui-helpers.js';
import { validateItem } from './validation.js';
import { initBossToggle, restrictDateInputs, populateBossDropdown } from './ui-helpers.js';

document.addEventListener("DOMContentLoaded", () => {
  restrictDateInputs();
  populateBossDropdown(BossAPI);

  
  initBossToggle();

  document.getElementById("username")?.addEventListener("blur", async (e) => {
    const username = e.target.value.trim();
    if (!username) return;
    try {
      const exists = await EmployeeAPI.checkUsername(username);
      if (exists) {
        alert(`The username "${username}" is already taken.`);
        e.target.value = "";
        e.target.focus();
      }
    } catch (err) {
      console.error("Username check failed", err);
    }
  });
});


window.saveData = async function() {
  const type = document.getElementById("item-type").value;
  const id = document.getElementById("item-id").value;
  const isCreate = document.getElementById("isCreate")?.value === "true";
  
  const employeeIdValue = document.getElementById("employee-id").value;
  const bossRoleValue = document.getElementById("emp-boss-role").value === "true";
  const hasBossValue = document.getElementById("emp-has-boss").value === "true";

 
  const payload = {
    employeeId: Number(employeeIdValue),
    employee: {
      employeeId: Number(employeeIdValue),
      bossRole: bossRoleValue,
      hasBoss: hasBossValue,
    },
    inUse: document.getElementById("inUse")?.checked ?? true,
  };


  if (type === "laptop") {
    if (!isCreate) payload.laptopId = Number(id);
    payload.osVersion = Number(document.getElementById("osVersion").value);
    payload.laptopYear = Number(document.getElementById("laptopYear").value);
    payload.lastOSUpdate = document.getElementById("lastOSUpdate").value;
    payload.needToUpdate = document.getElementById("needToUpdate").checked;
    payload.toRenew = document.getElementById("toRenew").checked;
  }

  if (type === "car") {
    if (!isCreate) payload.carId = Number(id);
    payload.milage = Number(document.getElementById("milage").value);
    payload.carYear = Number(document.getElementById("carYear").value);
    payload.lastServiced = document.getElementById("lastServiced").value;
    payload.needToServiceDate = document.getElementById("needToServiceDate").value;
    payload.lastInsuranceRenewal = document.getElementById("lastInsuranceRenewal").value;
    payload.insuranceExpireDate = document.getElementById("insuranceExpireDate").value;
    payload.toService = document.getElementById("toService").checked;
    payload.toRenewInsurance = document.getElementById("toRenewInsurance").checked;
    payload.toReplace = document.getElementById("toReplace").checked;
  }

  if (type === "id-card") {
    if (!isCreate) payload.idCardId = Number(id);
    payload.lastRenewedDate = document.getElementById("lastRenewedDate").value;
    payload.needToRenewDate = document.getElementById("needToRenewDate").value;
    payload.toRenew = document.getElementById("toRenew").checked;
  }


  const error = validateItem(type, payload);
  if (error) return alert(error);


  try {
    await ItemAPI.save(type, payload, isCreate);
    window.location.href = "/bossdashboard";
  } catch (err) {
    alert("Save failed: " + err.message);
  }
};


window.saveEmployee = async function() {
  const form = document.getElementById("employee-form");
  if (form && !form.checkValidity()) return form.reportValidity();

  const hasBoss = document.getElementById("hasBoss").checked;
  const bossUserId = document.getElementById("bossUserId")?.value;

  if (hasBoss && !bossUserId) {
    alert("Please select a boss.");
    return;
  }

  const payload = {
    name: document.getElementById("name").value.trim(),
    title: document.getElementById("title").value.trim(),
    username: document.getElementById("username").value.trim(),
    password: document.getElementById("password").value,
    email: document.getElementById("email").value.trim(),
    bossRole: document.getElementById("bossRole").checked,
    hasBoss: hasBoss,
    bossUserId: bossUserId ? Number(bossUserId) : null,
  };

  try {
    await EmployeeAPI.create(payload);
    window.location.href = "/bossdashboard";
  } catch (err) {
    handleApiError(err.message);
  }
};


window.deleteItem = async function(type, id) {
  if (!confirm(`Are you sure you want to delete this ${type}?`)) return;
  try {
    await ItemAPI.delete(type, id);
    window.location.reload();
  } catch (err) {
    console.error("Delete failed:", err);
  }
};

window.getMail = async () => {
    try { 
      await BossAPI.getMail(); 
      console.log("Mail sync triggered successfully"); 
    } catch (err) { 
      console.error("Mail error:", err); 
    }
};