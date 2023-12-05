import React, { useState, useEffect } from 'react';
import {
  Table,
  TableContainer,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Paper,
  Typography,
  Button,
} from '@mui/material';
import store from '../../component/catche/store';
import { useParams } from "react-router-dom/cjs/react-router-dom.min";

// const mockBookings = [{
//   id: "bookingID",
//   customerEmail: "customerEmail",
//   eventID: "eventID",
//   eventName: "event",
//   sectionID: "sectionID",
//   sectionType: "sectionType",
//   numTickets: 3
// }]

function ViewBookingsByEvent(props) {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);
  const { eventID } = useParams();
  const token = store.getToken(); // Get the user's role from local storage

  useEffect(() => {
    fetch(
      `${process.env.REACT_APP_API_URL}/view-booking?mode=byEvent&id=${eventID}`, {
        method: "GET",
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      }
    ).then(res =>res.json()
    ).then(data => setBookings(data))
    .catch(err => console.error('Error retrieving bookings:', err));
  }, []);

  const handleCancelBooking = (bookingID) => {
    fetch(
      `${process.env.REACT_APP_API_URL}/delete-booking?id=${bookingID}`, {
        method: "DELETE",
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      }
    ).then(res => 
      setBookings(bookings => bookings.filter(b => b.id != bookingID))
    ).catch(err => console.error('Error deleting booking:', err));
  }

  return (
    <div style={{margin: "5%"}}>
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        Event {eventID}'s Bookings
      </Typography>
      {loading ? (
        <Typography>Loading...</Typography>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Customer</TableCell>
                <TableCell>Section</TableCell>
                <TableCell>No. of Tickets</TableCell>
                <TableCell>Action</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {bookings.map((booking) => (
                <TableRow key={booking.id}>
                  <TableCell>{`${booking.customerEmail}`}</TableCell>
                  <TableCell>{`name: ${booking.eventName} - section: ${booking.sectionType}`}</TableCell>
                  <TableCell>{`${booking.numTickets}`}</TableCell>
                  <TableCell>
                    <Button
                      variant="outlined"
                      color="secondary"
                      onClick={() => handleCancelBooking(booking.id)}
                    >
                      Cancel Booking
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </div>
  );
}

export default ViewBookingsByEvent;
