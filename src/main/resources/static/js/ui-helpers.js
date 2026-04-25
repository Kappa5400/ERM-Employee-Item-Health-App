
export function restrictDateInputs() {
  const today = new Date().toISOString().split('T')[0];
  const now = new Date().toISOString().slice(0, 16);


  ["lastOSUpdate", "lastRenewedDate", "lastServiced", "lastInsuranceRenewal"].forEach(id => {
    // el is page element
    const el = document.getElementById(id);
    if (el) el.max = (id === "lastOSUpdate") ? now : today;
  });


  ["needToRenewDate", "needToServiceDate", "insuranceExpireDate"].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.min = today;
  });
}

export async function populateBossDropdown(BossAPI) {
  const select = document.getElementById("bossUserId");
  if (!select) return;

  try {
    const bosses = await BossAPI.getAll();
    select.innerHTML = '<option value="">-- Select a Boss --</option>';
    bosses.forEach((boss) => {
      const id = boss.employeeId || boss.id;
      const option = new Option(`${boss.name} (${boss.title || "No Title"})`, id);
      select.add(option);
    });
  } catch (error) {
    console.error("UI Error: Failed to load bosses", error);
  }
}

export function handleApiError(errorMsg, usernameInputId = "username") {
  alert("Action failed:\n\n" + errorMsg);
  if (errorMsg.toLowerCase().includes("already taken")) {
    const userBox = document.getElementById(usernameInputId);
    if (userBox) {
      userBox.value = "";
      userBox.focus();
    }
export function initBossToggle() {
  const hasBossToggle = document.getElementById("hasBoss");
  const bossSelect = document.getElementById("bossUserId");

  if (!hasBossToggle || !bossSelect) return; 

  const syncState = () => {
    bossSelect.disabled = !hasBossToggle.checked;
    if (bossSelect.disabled) {
      bossSelect.classList.add("bg-light");
      bossSelect.value = "";
    } else {
      bossSelect.classList.remove("bg-light");
    }
  };
  syncState();
  hasBossToggle.addEventListener("change", syncState);
}

}
}
