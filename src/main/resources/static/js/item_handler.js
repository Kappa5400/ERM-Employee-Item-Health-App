document.addEventListener("DOMContentLoaded", () => {
  loadBosses();
});

async function loadBosses() {
  const selectElement = document.getElementById("bossUserId");
  if (!selectElement) return;

  try {
    const response = await fetch("/api/boss");
    if (!response.ok) throw new Error("Network response was not ok");
    const bosses = await response.json();

    // Clear and fill
    selectElement.innerHTML = '<option value="">-- Select a Boss --</option>';
    bosses.forEach((boss) => {
      const option = document.createElement("option");
      console.log(boss);
      // Use a fallback logic: if 'employeeId' is missing, try 'id'
      const id = boss.employeeId || boss.id;

      // If 'name' is missing, try 'fullName'. If 'title' is missing, try 'jobTitle'
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

/**
 * Logic Checkpoint 4:
 * Always handle UI state. If 'Reports to Boss' is unchecked,
 * the select menu should be hidden and its value cleared.
 */
function setupBossToggle() {
  const hasBossCheckbox = document.getElementById("hasBoss");
  const bossWrapper = document.getElementById("boss-selection-wrapper");
  const bossSelect = document.getElementById("bossUserId");

  if (hasBossCheckbox && bossWrapper) {
    hasBossCheckbox.addEventListener("change", (e) => {
      if (e.target.checked) {
        bossWrapper.style.display = "block";
      } else {
        bossWrapper.style.display = "none";
        bossSelect.value = ""; // Clear selection on hide
      }
    });
  }
}

async function saveData() {
  const type = document.getElementById("item-type").value;
  const id = document.getElementById("item-id").value;
  const employeeIdValue = document.getElementById("employee-id").value;
  const bossRoleValue =
    document.getElementById("emp-boss-role").value === "true";
  const hasBossValue = document.getElementById("emp-has-boss").value === "true";
  const isCreate = document.getElementById("isCreate")?.value === "true";
  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;

  const payload = {
    employeeId: Number(employeeIdValue),
    employee: {
      employeeId: Number(employeeIdValue),
      bossRole: bossRoleValue,
      hasBoss: hasBossValue,
    },
    // 全タイプ共通のステータス
    inUse: document.getElementById("inUse")?.checked ?? true,
  };

  // Laptop固有の属性
  if (type === "laptop") {
    if (!isCreate) payload.laptopId = Number(id);
    payload.osVersion = Number(document.getElementById("osVersion").value);
    payload.laptopYear = Number(document.getElementById("laptopYear").value);
    payload.lastOSUpdate = document.getElementById("lastOSUpdate").value;
    payload.needToUpdate = document.getElementById("needToUpdate").checked;
    payload.toRenew = document.getElementById("toRenew").checked;
  }

  // Car固有の属性
  if (type === "car") {
    if (!isCreate) payload.carId = Number(id);
    payload.milage = Number(document.getElementById("milage").value);
    payload.carYear = Number(document.getElementById("carYear").value);
    payload.lastServiced = document.getElementById("lastServiced").value;
    payload.needToServiceDate =
      document.getElementById("needToServiceDate").value;
    payload.lastInsuranceRenewal = document.getElementById(
      "lastInsuranceRenewal",
    ).value;
    payload.insuranceExpireDate = document.getElementById(
      "insuranceExpireDate",
    ).value;
    payload.toService = document.getElementById("toService").checked;
    payload.toRenewInsurance =
      document.getElementById("toRenewInsurance").checked;
    payload.toReplace = document.getElementById("toReplace").checked;
  }

  // IDCard固有の属性
  if (type === "id-card") {
    if (!isCreate) payload.idCardId = Number(id);
    payload.lastRenewedDate = document.getElementById("lastRenewedDate").value;
    payload.needToRenewDate = document.getElementById("needToRenewDate").value;
    payload.toRenew = document.getElementById("toRenew").checked;
  }

  console.log("Sending to:", `/api/${type}`, "Payload:", payload);

  const method = isCreate ? "POST" : "PATCH";

  const response = await fetch(`/api/${type}`, {
    method: method,
    headers: {
      "Content-Type": "application/json",
      "X-CSRF-TOKEN": csrfToken, // CSRF対策
    },
    body: JSON.stringify(payload),
  });

  if (response.ok) {
    window.location.href = "/bossdashboard";
  } else {
    const errorMsg = await response.text();
    console.error("Server Error:", errorMsg);
    alert(
      "保存に失敗しました。詳細はコンソールを確認してください。: " + errorMsg,
    );
  }
}

/**
 * アイテム（Laptop, Car, IDCard）の削除
 */
async function deleteItem(type, id) {
  // 1. 確認ダイアログ
  if (!confirm(`本当にこの ${type} を削除してもよろしいですか？`)) return;

  // 2. CSRFトークンの取得
  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;

  // 3. URLの構築 (REST APIの標準的な形式: /api/laptop/123)
  const url = `/api/${type}/${id}`;

  console.log(`Sending DELETE to: ${url}`);

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "X-CSRF-TOKEN": csrfToken, // Spring Security対策
      },
    });

    if (response.ok) {
      // 削除成功：ダッシュボードをリロード
      window.location.reload();
    } else {
      const errorText = await response.text();
      console.error("Delete Error:", errorText);
      alert("削除に失敗しました。サーバーログを確認してください。");
    }
  } catch (error) {
    console.error("Network error:", error);
    alert("通信エラーが発生しました。");
  }
}

async function deleteEmployee(id, name) {
  if (
    !confirm(
      `従業員 "${name}" を削除しますか？関連するアイテムも削除される可能性があります。`,
    )
  )
    return;

  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;
  const url = `/api/employee/${id}`;

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "X-CSRF-TOKEN": csrfToken,
      },
    });

    if (response.ok) {
      window.location.href = "/bossdashboard";
    } else {
      alert("削除に失敗しました。");
    }
  } catch (error) {
    console.error("Error:", error);
  }
}

async function saveEmployee() {
  // 1. Get inputs using the correct IDs from the HTML
  const name = document.getElementById("name").value;
  const title = document.getElementById("title").value;
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const email = document.getElementById("email").value;
  const bossRole = document.getElementById("bossRole").checked;
  const hasBoss = document.getElementById("hasBoss").checked;

  // 2. Capture the selected Boss ID
  // If 'hasBoss' is false, we set this to null
  const bossUserId = hasBoss
    ? document.getElementById("bossUserId").value
    : null;

  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;

  // 3. Construct the payload
  const payload = {
    name: name,
    title: title,
    username: username,
    password: password,
    email: email,
    bossRole: bossRole,
    hasBoss: hasBoss,
    bossUserId: bossUserId ? Number(bossUserId) : null, // Ensure it's a number or null
  };

  console.log("Creating Employee with Boss:", payload);

  try {
    const response = await fetch("/api/employee", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-CSRF-TOKEN": csrfToken,
      },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
      window.location.href = "/bossdashboard";
    } else {
      const errorMsg = await response.text();
      alert("Failed to save employee: " + errorMsg);
    }
  } catch (error) {
    console.error("Network error:", error);
  }
}

async function getMail() {
  const url = "/api/getmail";

  try {
    const response = await fetch(url);
    // ... rest of your logic
  } catch (error) {
    console.error("Error:", error);
  }
}
