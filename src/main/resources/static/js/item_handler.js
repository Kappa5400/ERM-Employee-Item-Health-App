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
