async function saveData() {
  const type = document.getElementById("item-type").value;
  const id = document.getElementById("item-id").value;
  const employeeIdValue = document.getElementById("employee-id").value;
  const employeeName = document.getElementById("employeename").value;
  const bossRoleValue =
    document.getElementById("emp-boss-role").value === "true";
  const hasBossValue = document.getElementById("emp-has-boss").value === "true";

  const isCreate = document.getElementById("isCreate")?.value === "true";

  const payload = {
    employeeId: Number(employeeIdValue),
    employee: {
      employeeId: Number(employeeIdValue),
      bossRole: bossRoleValue,
      hasBoss: hasBossValue,
    },
  };

  if (type === "laptop") {
    if (isCreate == false) {
      payload.laptopId = Number(id);
    }

    payload.osVersion = Number(
      document.querySelector('input[name="osVersion"]').value,
    );
    payload.laptopYear = Number(
      document.querySelector('input[name="laptopYear"]').value,
    );
    payload.needToUpdate = false;
    payload.toRenew = false;
    payload.inUse = true;
  }

  if (type === "car") {
    if (!isCreate) payload.carId = Number(id);

    payload.milage = Number(
      document.querySelector('input[name="milage"]').value,
    );
    payload.carYear = Number(
      document.querySelector('input[name="carYear"]').value,
    );
    payload.inUse = true;
    payload.toService = false;
    payload.toRenewInsurance = false;
    payload.toReplace = false;
  }

  if (type === "id-card") {
    if (!isCreate) {
      payload.idCardId = Number(id);
    }
    payload.needToRenewDate = document.querySelector(
      'input[name="needToRenewDate"]',
    ).value;

    const today = new Date().toISOString().split("T")[0]; // Gets 'YYYY-MM-DD'
    payload.lastRenewedDate = today;

    payload.inUse = true;
    payload.toRenew = false;
  }

  console.log("Sending to:", `/api/${type}`, "Payload:", payload);

  const method = isCreate ? "POST" : "PATCH";

  const response = await fetch(`/api/${type}`, {
    method: method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (response.ok) {
    window.location.href = "/bossdashboard";
  } else {
    const errorMsg = await response.text();
    console.error("Server Error:", errorMsg);
    alert("Failed to save. Check the console for: " + errorMsg);
  }
}

async function deleteItem(type, id) {
  if (!confirm(`Are you sure you want to delete this ${type}?`)) return;

  // CSRF token is mandatory for DELETE requests in Spring Security
  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;

  // Construct URL to match @DeleteMapping("/{type}/{id}")
  const url = `/api/${type}/${id}`;

  console.log(`Sending DELETE to: ${url}`);

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "X-CSRF-TOKEN": csrfToken,
      },
    });

    if (response.ok) {
      window.location.reload();
    } else {
      const errorText = await response.text();
      console.error("Delete Error:", errorText);
      alert("Delete failed. Check server console.");
    }
  } catch (error) {
    console.error("Network error:", error);
  }
}

async function saveEmployee() {
  const name = document.getElementById("emp-name").value;
  const title = document.getElementById("emp-title").value; // Grab title
  const email = document.getElementById("emp-email").value; // Grab email
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const isBoss = document.getElementById("emp-boss-role").checked;
  const hasBoss = document.getElementById("emp-has-boss").checked;
  const token = document.querySelector('input[name="_csrf"]')?.value;

  const payload = {
    name: name,
    title: title,
    email: email,
    username: username,
    password: password,
    bossRole: isBoss,
    hasBoss: hasBoss,
  };

  if (!name || !title || !username || !password) {
    alert(
      "Please fill in all required fields (Name, Title, Username, Password)",
    );
    return;
  }

  try {
    const response = await fetch("/api/employee", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-CSRF-TOKEN": token,
      },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
      window.location.href = "/bossdashboard";
    } else {
      const err = await response.text();
      alert("Error: " + err);
    }
  } catch (e) {
    console.error("Connection error:", e);
  }
}

async function deleteEmployee(id, name) {
  // 受け取った name を確認ダイアログに表示
  if (!confirm(`Are you sure you want to delete ${name}?`)) return;

  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;

  // URLに従業員のIDを含める。パスは通常 /api/employee/{id} になります
  const url = `/api/employee/${id}`;

  console.log(`Sending DELETE to: ${url}`);

  try {
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "X-CSRF-TOKEN": csrfToken,
      },
    });

    if (response.ok) {
      // 削除後はその従業員の詳細ページは存在しないため、ダッシュボードに戻るのが一般的です
      window.location.href = "/bossdashboard";
    } else {
      const errorText = await response.text();
      console.error("Delete Error:", errorText);
      alert("Delete failed. Check server console.");
    }
  } catch (error) {
    console.error("Network error:", error);
  }
}
