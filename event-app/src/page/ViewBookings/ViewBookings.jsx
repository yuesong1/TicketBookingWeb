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

function ViewBooking() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const userRole = store.getRole();
  const token = store.getToken(); // Get the user's role from local storage
  

  useEffect(() => {
    // Define the API endpoint based on the user's role
    const apiEndpoint =
      userRole === 'admin'
        ? 'http://your-api-url-here/view-booking?mode=admin_mode'
        : userRole === 'planner'
        ? 'http://your-api-url-here/view-booking?mode=planner_mode&plannerEmail={userID}'
        : 'http://your-api-url-here/view-booking?mode=by_customer&customerEmail={userID}';

    // Fetch user's bookings from the API
    fetch(apiEndpoint, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        // Include any necessary authentication headers (e.g., token) here
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else if (response.status === 401) {
          throw new Error('Unauthorized');
        } else if (response.status === 404) {
          throw new Error('User not found');
        }
      })
      .then((data) => {
        setBookings(data);
        setLoading(false);
      })
      .catch((error) => console.error('Error fetching bookings:', error));
  }, [userRole]);

  const handleCancelBooking = (bookingID) => {
    // Fetch the API endpoint based on the user's role
    const apiEndpoint =
      userRole === 'admin'
        ? `http://your-api-url-here/delete-booking?id=${bookingID}&mode=admin_mode`
        : userRole === 'planner'
        ? `http://your-api-url-here/delete-booking?id=${bookingID}&mode=planner_mode`
        : `http://your-api-url-here/delete-booking?id=${bookingID}&mode=customer_mode`;

    // Send a DELETE request to cancel the booking
    fetch(apiEndpoint, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (response.status === 200) {
          // Booking canceled successfully, update the UI accordingly
          setBookings((prevBookings) =>
            prevBookings.filter((booking) => booking.bookingID !== bookingID)
          );
        } else if (response.status === 401) {
          throw new Error('Unauthorized');
        } else if (response.status === 404) {
          throw new Error('User not found');
        }
      })
      .catch((error) => console.error('Error canceling booking:', error));
  };

  return (
    <div>
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        My Bookings
      </Typography>
      {loading ? (
        <Typography>Loading...</Typography>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Event ID</TableCell>
                <TableCell>Ticket Details</TableCell>
                <TableCell>Action</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {bookings.map((booking) => (
                <TableRow key={booking.bookingID}>
                  <TableCell>{booking.eventID}</TableCell>
                  <TableCell>
                    <ul>
                      {booking.ticketDetails.map((ticket, index) => (
                        <li key={index}>
                          Section ID: {ticket.sectionID}, Details: {ticket.details}
                        </li>
                      ))}
                    </ul>
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="outlined"
                      color="secondary"
                      onClick={() => handleCancelBooking(booking.bookingID)}
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

export default ViewBooking;
