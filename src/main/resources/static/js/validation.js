

export function validateItem(type, payload) {
  const now = new Date();

  if (type === "laptop" && new Date(payload.lastOSUpdate) > now) {
    return "Error: Last OS Update cannot be in the future.";
  }

  if (type === "car") {
    const lastSvc = new Date(payload.lastServiced);
    const nextSvc = new Date(payload.needToServiceDate);
    if (lastSvc > now) return "Error: Last Serviced Date cannot be in the future.";
    if (lastSvc > nextSvc) return "Error: Last Serviced Date cannot be after the Next Service Due date.";
  }

  if (type === "id-card") {
    const lastRenew = new Date(payload.lastRenewedDate);
    const nextDue = new Date(payload.needToRenewDate);
    if (lastRenew > now) return "Error: Last Renewed Date cannot be in the future.";
    if (lastRenew && nextDue && lastRenew > nextDue) {
      return "Error: Last Renewed must be before the Next Renewal.";
    }
  }

  return null; 
}