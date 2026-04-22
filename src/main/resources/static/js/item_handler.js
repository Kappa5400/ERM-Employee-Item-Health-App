/**
 * ----------------------------------------------------
 * Initialization & UI Helpers
 * ----------------------------------------------------
 */

document.addEventListener("DOMContentLoaded", () => {
  loadBosses();
  setupBossToggle();
  restrictDateInputs(); // Block impossible dates in the UI

  // --- Real-time Username Checker ---
  const usernameInput = document.getElementById("username");
  if (usernameInput) {
    usernameInput.addEventListener("blur", async (e) => {
      const username = e.target.value.trim();
      if (!username) return; 

      try {
        const response = await fetch(`/api/employee/check-username?username=${username}`);
        const exists = await response.json();

        if (exists) {
          alert(`The username "${username}" is already taken. Please choose another.`);
          e.target.value = ""; 
          e.target.focus();    
        }
      } catch (error) {
        console.error("Error checking username:", error);
      }
    });
  }
});

const getCsrf = () => ({
  token: document.querySelector('meta[name="_csrf"]')?.getAttribute("content"),
  header: document.querySelector('meta[name="_csrf_header"]')?.getAttribute("content")
});

/**
 * Restricts the HTML5 date pickers to prevent selecting illogical dates.
 */
function restrictDateInputs() {
  const today = new Date().toISOString().split('T')[0];

  // 1. Laptop: Last OS Update cannot be tomorrow
  const laptopLastUpdate = document.getElementById("lastOSUpdate");
  if (laptopLastUpdate) laptopLastUpdate.max = new Date().toISOString().slice(0, 16);

  // 2. ID Card / Car: Last events cannot be in the future
  ["lastRenewedDate", "lastServiced", "lastInsuranceRenewal"].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.max = today;
  });

  // 3. ID Card / Car: Expiry/Next Due should typically be today or later
  ["needToRenewDate", "needToServiceDate", "insuranceExpireDate"].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.min = today;
  });
}

async function loadBosses() {
  const selectElement = document.getElementById("bossUserId");
  if (!selectElement) return;

  try {
    const response = await fetch("/api/boss");
    if (!response.ok) throw new Error("Network response was not ok");
    const bosses = await response.json();

    selectElement.innerHTML = '<option value="">-- Select a Boss --</option>';
    bosses.forEach((boss) => {
      const option = document.createElement("option");
      const id = boss.employeeId || boss.id;
      const displayName = boss.name || boss.fullName || "Unknown";
      const displayTitle = boss.title || boss.jobTitle || "No Title";

      option.value = id;
      option.textContent = `${displayName} (${displayTitle})`;
      selectElement.appendChild(option);
    });
  } catch (error) {
    console.error("Error fetching bosses:", error);
  }
}

function setupBossToggle() {
  // Left intentionally blank to let the HTML inline script handle the UI gracefully
}

/**
 * ----------------------------------------------------
 * SAVE DATA (Create & Update Items)
 * ----------------------------------------------------
 */

async function saveData() {
  const type = document.getElementById("item-type").value;
  const id = document.getElementById("item-id").value;
  const isCreate = document.getElementById("isCreate")?.value === "true";
  const now = new Date();

  // --- MINIMUM VALIDATION GATEKEEPER ---
  let requiredFields = [];
  if (type === "laptop") requiredFields = ["laptopYear", "osVersion", "lastOSUpdate"];
  if (type === "car") requiredFields = ["carYear", "milage", "lastServiced", "needToServiceDate", "lastInsuranceRenewal", "insuranceExpireDate"];
  if (type === "id-card") requiredFields = ["lastRenewedDate", "needToRenewDate"];

  for (let fieldId of requiredFields) {
    const el = document.getElementById(fieldId);
    if (el && !el.value.trim()) {
      alert("Validation Error: Please fill in all required fields.");
      el.focus();
      return; // Stop the save
    }
  }
  // --------------------------------------

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

  // Laptop Logic
  if (type === "laptop") {
    const lastUpdateVal = document.getElementById("lastOSUpdate").value;
    if (new Date(lastUpdateVal) > now) {
      alert("Error: Last OS Update cannot be in the future.");
      return;
    }
    if (!isCreate) payload.laptopId = Number(id);
    payload.osVersion = Number(document.getElementById("osVersion").value);
    payload.laptopYear = Number(document.getElementById("laptopYear").value);
    payload.lastOSUpdate = lastUpdateVal;
    payload.needToUpdate = document.getElementById("needToUpdate").checked;
    payload.toRenew = document.getElementById("toRenew").checked;
  }

  // Car Logic
  if (type === "car") {
    const lastSvc = new Date(document.getElementById("lastServiced").value);
    const nextSvc = new Date(document.getElementById("needToServiceDate").value);
    const lastIns = new Date(document.getElementById("lastInsuranceRenewal").value);
    const nextIns = new Date(document.getElementById("insuranceExpireDate").value);

    if (lastSvc > now) { alert("Error: Last Serviced Date cannot be in the future."); return; }
    if (lastSvc > nextSvc) { alert("Error: Last Serviced Date cannot be after the Next Service Due date."); return; }
    if (lastIns > nextIns) { alert("Error: Last Insurance Renewal cannot be after the Expiry Date."); return; }

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

  // ID Card Logic
  if (type === "id-card") {
    const lastRenewVal = document.getElementById("lastRenewedDate").value;
    const nextDueVal = document.getElementById("needToRenewDate").value;

    if (lastRenewVal && nextDueVal) {
      const lastRenew = new Date(lastRenewVal);
      const nextDue = new Date(nextDueVal);

      if (lastRenew > now) {
        alert("Error: Last Renewed Date cannot be in the future.");
        return;
      }
      if (lastRenew > nextDue) {
        alert("Error: Last Renewed Date must be before the Next Renewal Due date.");
        return;
      }
    }

    if (!isCreate) payload.idCardId = Number(id);
    payload.lastRenewedDate = lastRenewVal;
    payload.needToRenewDate = nextDueVal;
    payload.toRenew = document.getElementById("toRenew").checked;
  }

  // --- EXECUTE FETCH ---
  const method = isCreate ? "POST" : "PATCH";
  const { token, header } = getCsrf();

  try {
    const response = await fetch(`/api/${type}`, {
      method: method,
      headers: { "Content-Type": "application/json", [header]: token },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
      window.location.href = "/bossdashboard";
    } else {
      const errorMsg = await response.text();
      alert("Save failed: " + errorMsg);
    }
  } catch (error) {
    console.error("Network error:", error);
  }
}

/**
 * ----------------------------------------------------
 * DELETE OPERATIONS
 * ----------------------------------------------------
 */

async function deleteItem(type, id) {
  if (!confirm(`Are you sure you want to delete this ${type}?`)) return;
  const { token, header } = getCsrf();
  
  try {
    const response = await fetch(`/api/${type}/${id}`, {
      method: "DELETE",
      headers: { [header]: token },
    });
    if (response.ok) window.location.reload();
  } catch (error) {
    console.error("Error:", error);
  }
}

async function deleteEmployee(id, name) {
  if (!confirm(`Delete employee "${name}"?`)) return;
  const { token, header } = getCsrf();
  
  try {
    const response = await fetch(`/api/employee/${id}`, {
      method: "DELETE",
      headers: { [header]: token },
    });
    if (response.ok) window.location.href = "/bossdashboard";
  } catch (error) {
    console.error("Error:", error);
  }
}

/**
 * ----------------------------------------------------
 * EMPLOYEE SAVE
 * ----------------------------------------------------
 */

async function saveEmployee() {
  // 1. THE NATIVE HTML GATEKEEPER
  const form = document.getElementById("employee-form"); 
  
  if (form && !form.checkValidity()) {
    form.reportValidity(); 
    return; 
  }

  const hasBoss = document.getElementById("hasBoss").checked;
  const bossUserId = hasBoss ? document.getElementById("bossUserId").value : null;

  if (hasBoss && !bossUserId) {
    alert("Validation Error: You checked 'Reports to Boss' but did not select a boss.");
    document.getElementById("bossUserId").focus();
    return;
  }
  // --------------------------------------

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

  const { token, header } = getCsrf();

  try {
    const response = await fetch("/api/employee", {
      method: "POST",
      headers: { "Content-Type": "application/json", [header]: token },
      body: JSON.stringify(payload),
    });
    
    if (response.ok) {
      window.location.href = "/bossdashboard";
    } else {
      // --- FIXED ERROR HANDLING ---
      // 1. Read the response body exactly ONCE as plain text
      let errorMsg = await response.text(); 
      
      // 2. See if it happens to be JSON. If not, just ignore and keep the text.
      try {
        const errorData = JSON.parse(errorMsg);
        if (errorData && errorData.message) {
          errorMsg = errorData.message;
        }
      } catch (e) {
        // It was plain text (which our GlobalExceptionHandler sends). Do nothing!
      }
      
      // Trigger the precise frontend alert!
      alert("Cannot create employee:\n\n" + errorMsg);
      
      // Clear the box and refocus if it's a duplicate username
      if (errorMsg.toLowerCase().includes("already taken")) {
          const userBox = document.getElementById("username");
          if (userBox) {
              userBox.value = "";
              userBox.focus();
          }
      }
    }
  } catch (error) {
    console.error("Fetch/Network Error:", error);
    alert("Network error: Could not reach the server.");
  }
}

async function getMail() {
  try {
    const response = await fetch("/api/getmail");
    if (response.ok) {
        console.log("Mail fetched successfully");
    }
  } catch (error) {
    console.error("Error:", error);
  }
}