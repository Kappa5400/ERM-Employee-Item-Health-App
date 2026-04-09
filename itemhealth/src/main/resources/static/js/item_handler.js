async function saveData() {
  const type = document.getElementById("item-type").value; // This will now be "laptop", "car", or "id-card"
  const id = document.getElementById("item-id").value;
  const employeeIdValue = document.getElementById("employee-id").value;
  const bossRoleValue =
    document.getElementById("emp-boss-role").value === "true";
  const hasBossValue = document.getElementById("emp-has-boss").value === "true";

  const payload = {
    employeeId: Number(employeeIdValue),
    employee: {
      employeeId: Number(employeeIdValue),
      bossRole: bossRoleValue,
      hasBoss: hasBossValue,
    },
  };

  if (type === "laptop") {
    payload.laptopId = Number(id);
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
    payload.carId = Number(id);
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

  // Use "id-card" to match the kebab-case you set in the HTML
  if (type === "id-card") {
    payload.idCardId = Number(id);
    payload.needToRenewDate = document.querySelector(
      'input[name="needToRenewDate"]',
    ).value;

    const today = new Date().toISOString().split("T")[0]; // Gets 'YYYY-MM-DD'
    payload.lastRenewedDate = today;

    payload.inUse = true;
    payload.toRenew = false;
  }

  console.log("Sending to:", `/api/${type}`, "Payload:", payload);

  const response = await fetch(`/api/${type}`, {
    method: "PATCH",
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
