import React, { useState } from 'react';
import { TextField, Button, Paper, Typography, Container } from '@mui/material';
import { useHistory } from "react-router-dom";
import { LockOutlined } from '@mui/icons-material';
import './Login.css';
import store from '../../component/catche/store';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

const Login = () => {
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const history = useHistory();

  const handleChange = (event) => {
    setRole(event.target.value);
  };

  const handleLogin = async () => {
    try {
        const response = await fetch(
            `${process.env.REACT_APP_API_URL}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: email,
                    password: password,
                    type: role
                }),
            }
        );

        if (response.ok) {
            const res = await response.json();
            console.log(res);
            store.setEmail(email);
            store.setToken(res.token);
            store.setRole(role);
            navAfterLogin(role);
        } else  {
            alert('Login failed, try again!');
        }
    } catch (error) {
        console.error('An error occurred while logging in', error);
    }
};


  const navAfterLogin = () => {
    switch (store.getRole()) {
      case "admin":
        history.push("/viewUsers");
        break;
      case "customer":
        history.push(`/viewBookings/byCustomer/${store.getEmail()}`);
        break;
      case "planner":
        history.push(`/viewEvents/byPlanner/${store.getEmail()}`)
        break;
    }
    window.location.reload();
  }

  return (
    <Container component="main" maxWidth="xs">
      <Paper elevation={3} className="login-paper">
        <LockOutlined />
        <Typography variant="h5">Login</Typography>

        
        <Box sx={{ minWidth: 120 }}>
      <FormControl fullWidth>
        <InputLabel id="demo-simple-select-label">Role</InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={role}
          label="Role"
          onChange={handleChange}
        >
          <MenuItem value={'admin'}>Admin</MenuItem>
          <MenuItem value={'planner'}>Event Planner</MenuItem>
          <MenuItem value={'customer'}>Customer</MenuItem>
        </Select>
      </FormControl>
    </Box>

        <TextField
          label="Email"
          variant="outlined"
          margin="normal"
          fullWidth
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <TextField
          label="Password"
          type="password"
          variant="outlined"
          margin="normal"
          fullWidth
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {error && <Typography color="error">{error}</Typography>}
        <Button
          variant="contained"
          color="primary"
          fullWidth
          onClick={handleLogin}
        >
          Sign In
        </Button>
      </Paper>
    </Container>
  );
};

export default Login;
