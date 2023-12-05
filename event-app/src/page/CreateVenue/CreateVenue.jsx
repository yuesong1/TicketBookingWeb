import React, { useState } from 'react';
import { Button, TextField, Typography } from '@mui/material';
import Box from '@mui/material/Box';
import './CreateVenue.css'; // Import the CSS file
import DeleteIcon from '@mui/icons-material/Delete';
import { useHistory } from 'react-router-dom';
import store from '../../component/catche/store';

function CreateVenue() {
  const [venueName, setVenueName] = useState('');
  const [venueLocation, setVenueLocation] = useState('');
  const [sections, setSections] = useState([{ type: '', capacity: '' }]);
  const history = useHistory();
  const token = store.getToken();

  const validateCapacity = (e, capacity) => {
    if (e.key === 'Backspace' || e.key === 'Delete') {
    } else if (!/^[0-9]$/.test(e.key) || Number(capacity + e.key) > 2147483647)
      e.preventDefault();
  };

  const handleSectionChange = (index, field, value) => {
    const updatedSections = [...sections];
    updatedSections[index][field] = value;
    setSections(updatedSections);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Check for empty input fields
    if (
      !venueName ||
      !venueLocation ||
      sections.some((section) => !section.type || !section.capacity)
    ) {
      console.log('Please fill in all the fields');
      return;
    }

    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/create-venue`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
          body: JSON.stringify({
            name: venueName,
            address: venueLocation,
            sections: sections,
          }),
        }
      );

      if (response.status === 201) {
        // Venue created successfully
        console.log('Venue created successfully');
        alert('Venue created successfully');
        history.push('./viewVenues');
      } else if (response.status === 400) {
        // Invalid request data
        console.error('Invalid request data');
        alert('Invalid request data');
      } else{
        alert('Failed to create Venue');
        console.error('An error occurred');
      }
    } catch (error) {
      alert('Invalid request data');
      console.error('An error occurred', error);
    }
  };

  const renderSections = () => {
    return sections.map((section, index) => (
      <div key={index}>
        <h3>Section {index + 1} : </h3>
        <TextField
          required
          label="Section Name"
          value={section.type}
          onChange={(e) => handleSectionChange(index, 'type', e.target.value)}
          variant="outlined"
        />
        <TextField
          required
          label="Section Capacity"
          value={section.capacity}
          type="number"
          max={2147483647}
          onKeyDown={(e) => validateCapacity(e, section.capacity)}
          onChange={(e) =>
            handleSectionChange(index, 'capacity', e.target.value)
          }
          InputLabelProps={{
            shrink: true,
          }}
        />
        <Button onClick={() => handleRemoveSection(index)}>
          <DeleteIcon fontSize="large" className="delete-buttons" />
        </Button>
      </div>
    ));
  };

  const handleAddSection = () => {
    setSections([...sections, { type: '', capacity: '' }]);
  };

  const handleRemoveSection = (index) => {
    const updatedSections = [...sections];
    updatedSections.splice(index, 1);
    setSections(updatedSections);
  };

  return (
    <div className="create-venue-container">
      <Box
        component="form"
        className="create-venue-form"
        onSubmit={handleSubmit}
        sx={{
          '& .MuiTextField-root': { m: 1, width: '70ch' },
        }}
        noValidate
        autoComplete="off"
        alignItems={'center'}
      >
        <Typography variant="h4" align="center">
          Create a New Venue
        </Typography>

        {/* Existing input fields */}
        <TextField
          label="Venue Name"
          value={venueName}
          onChange={(e) => setVenueName(e.target.value)}
          variant="outlined"
          required
        />
        <TextField
          required
          label="Location"
          value={venueLocation}
          onChange={(e) => setVenueLocation(e.target.value)}
          multiline
          maxRows={4}
        />

        {/* Sections */}
        {renderSections()}
        <div className="section-buttons">
          <Button onClick={handleAddSection} variant="outlined">
            Add Section
          </Button>
          <Button type="submit" variant="contained" color="primary">
            Create Venue
          </Button>
        </div>
      </Box>
    </div>
  );
}

export default CreateVenue;
