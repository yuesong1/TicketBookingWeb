import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
} from '@mui/material';
import { useHistory } from 'react-router-dom';
import store from '../../component/catche/store';

const mockUsers = {
  admin: [],
  customers: [],
  planners: [],
}

function ViewUsers() {
  const [users, setUsers] = useState(mockUsers);
  const history = useHistory();
  const token = store.getToken();

  useEffect(() => {
    // Fetch users data from the API
    fetch(
      `${process.env.REACT_APP_API_URL}/view-user?mode=all`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          throw new Error('Unauthorized');
        }
      })
      .then((data) => setUsers(data))
      .catch((error) => console.error('Error fetching users:', error));
  }, [token]);

  const handleViewBooking = (id) => {
    // Redirect to the View Booking page with user ID
    history.push(`./viewBookings/byCustomer/${id}`);
  };

  const handleViewEvents = (id) => {
    history.push(`./viewEvents/byPlanner/${id}`)
  }

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        View Users
      </Typography>
      <Typography variant="h5" sx={{ mt: 2, mb: 1 }}>
        Customers
      </Typography>
      <TableContainer component={Paper} sx={{ marginBottom: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Email</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.customers.map((customer) => (
              <TableRow key={customer.email}>
                <TableCell>{customer.email}</TableCell>
                <TableCell>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={() => handleViewBooking(customer.email)}
                  >
                    View Booking
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Typography variant="h5" sx={{ mt: 2, mb: 1 }}>
        Planners
      </Typography>
      <TableContainer component={Paper} sx={{ marginBottom: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Email</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.planners.map((planner) => (
              <TableRow key={planner.email}>
                <TableCell>{planner.email}</TableCell>
                <TableCell>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={() => handleViewEvents(planner.email)}
                  >
                    View Events
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
}

export default ViewUsers;
