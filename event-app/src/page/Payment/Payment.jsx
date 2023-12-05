import React, { useState } from 'react';
import {
  Container,
  Typography,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Grid,
  Box,
} from '@mui/material';
import PaymentIcon from '@mui/icons-material/Payment';

const Payment = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('');

  const handleNameChange = (event) => {
    setName(event.target.value);
  };

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const handlePaymentMethodChange = (event) => {
    setPaymentMethod(event.target.value);
  };

  return (
    <Container maxWidth="sm">
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        Payment Details
      </Typography>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <PaymentIcon sx={{ mr: 1 }} />
        <Typography variant="h6">Select Payment Method:</Typography>
      </Box>
      <FormControl fullWidth>
        <InputLabel id="payment-method-label">Payment Method</InputLabel>
        <Select
          labelId="payment-method-label"
          id="payment-method"
          value={paymentMethod}
          label="Payment Method"
          onChange={handlePaymentMethodChange}
        >
          <MenuItem value="creditCard">Credit Card</MenuItem>
          <MenuItem value="paypal">PayPal</MenuItem>
          {/* Add more payment options as needed */}
        </Select>
      </FormControl>
      <TextField
        label="Name"
        fullWidth
        value={name}
        onChange={handleNameChange}
        sx={{ mt: 2 }}
      />
      <TextField
        label="Email"
        fullWidth
        value={email}
        onChange={handleEmailChange}
        sx={{ mt: 2 }}
      />
      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={6}>
          {/* Add your payment method icons here */}
          {paymentMethod === 'creditCard' && (
            <PaymentIcon color="primary" fontSize="large" />
          )}
          {paymentMethod === 'paypal' && (
            <img src="paypal-icon.png" alt="PayPal" />
          )}
          {/* Add more icons for other payment methods */}
        </Grid>
      </Grid>
      <Button
        variant="contained"
        color="primary"
        size="large"
        fullWidth
        sx={{ mt: 3 }}
      >
        Proceed to Payment
      </Button>
    </Container>
  );
};

export default Payment;
