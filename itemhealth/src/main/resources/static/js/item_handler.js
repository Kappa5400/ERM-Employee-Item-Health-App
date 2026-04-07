async function saveData() {
  const type = document.getElementById("item-type").value;
  const id = document.getElementById("item-id").value;

  // Build the object to match your Java Model (Laptop.java / Car.java)
  const payload = {};

  if (type === "laptop") {
    payload.laptopId = id;
    payload.osVersion = document.querySelector('input[name="osVersion"]').value;
    payload.laptopYear = document.querySelector(
      'input[name="laptopYear"]',
    ).value;
  }

  if (type === "car") {
    payload.carId = Number(id);
    payload.milage = Number(
      document.querySelector('input[name="milage"]').value,
    );
    payload.carYear = Number(
      document.querySelector('input[name="carYear"]').value,
    );
  }

  if (type === "idcard") {
    payload.idcard = Number(id);
    payload.needToRenewDate = Number(
      document.querySelector('input[name="needToRenewDate"]').value,
    );
  }

  // Add logic for 'car' or 'idcard' here...

  const response = await fetch(`/api/${type}`, {
    method: "PATCH", // Matches your @PatchMapping in Controller
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (response.ok) {
    window.location.href = "/bossdashboard"; // Redirect on success
  } else {
    alert("Failed to save. Check the browser console (F12) for errors.");
  }
}
