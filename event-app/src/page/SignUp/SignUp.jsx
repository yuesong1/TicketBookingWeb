import React, { useState } from 'react';
import { TextField, Button, Paper, Typography, Container } from '@mui/material';
import { LockOutlined } from '@mui/icons-material';
import './SignUp.css';
import RoleSelector from '../../component/roleSelector';
import store from '../../component/catche/store';
import { useHistory } from 'react-router-dom';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

const SignUp = () => {
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const history = useHistory();

  const handleChange = (event) => {
    setRole(event.target.value);
  };

  const handleSignUp = async () => {

    try {
      var input = {
        email: email, 
        type: role, 
        password:password
      };

      console.log(input)
      const response = await fetch(
        process.env.REACT_APP_API_URL != null ? `${process.env.REACT_APP_API_URL}/sign-up` : 
        'http://localhost:8080/event_backend_war_exploded/sign-up', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(input),
      });

      if (response.ok) {
        // Redirect or perform other actions after successful signup
        alert('Sign Up Successfully')
        history.push('./login');
      } else {
        alert('Sign Up Unsuccessfully')
        // Handle other error cases
        setError('An error occurred');
      }
    } catch (error) {
      console.error('Error during signup:', error);
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Paper elevation={3} className="signup-paper">
        <LockOutlined />
        <Typography variant="h5">Sign up</Typography>

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
          type="email"
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
          onClick={handleSignUp}
        >
          Sign Up
        </Button>
      </Paper>
    </Container>
  );
};

export default SignUp;
