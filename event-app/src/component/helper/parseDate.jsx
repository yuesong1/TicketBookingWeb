import dayjs from "dayjs";

function parseDateTimeString(dateTimeString) {
    const [datePart, timePart] = dateTimeString.split(' ');
    const [year, month, day] = datePart.split('-').map(Number);
    const [hour, minute] = timePart.split(':').map(Number);
  
    // Months are 0-indexed in JavaScript, so subtract 1 from the month
    const date = new Date(year, month - 1, day, hour, minute);
    return dayjs(date)
  }
export default parseDateTimeString ;