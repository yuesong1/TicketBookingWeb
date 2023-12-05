import React, { useState, useEffect } from 'react';
import { Button, TextField, Typography } from '@mui/material';
import Box from '@mui/material/Box';
import { useHistory, useParams } from 'react-router-dom';
import DeleteIcon from '@mui/icons-material/Delete';
import '../CreateVenue/CreateVenue.css';
import store from '../../component/catche/store';

const mockVenue = 
  { "id" : '', "name": "", "address": "", "sections": [ { "id": "s1", "type": "Seated", "capacity": 100 } ] }

function EditVenue() {
  const { id } = useParams();
  const history = useHistory();
  
  // Fetch specific venue by its ID from local storage
  const fetchedVenue = store.getVenueByID(id) || mockVenue;

  const [venueName, setVenueName] = useState(fetchedVenue.name);
  const [venueLocation, setVenueLocation] = useState(fetchedVenue.address);
  const [sections, setSections] = useState(fetchedVenue.sections);
  const token = store.getToken();

  // useEffect(() => {
  //   // Fetch venue data from the API using the provided ID
  
  //   fetch(
  //     `${process.env.REACT_APP_API_URL}/view-venue?mode=one&id=${id}`, {
  //     method: 'GET',
  //     headers: {
  //       'Content-Type': 'application/json',
  //       'Authorization': `Bearer ${token}`
  //     },
  //   })
  //     .then((response) => {
  //       if (response.status === 400) {
  //         throw new Error('Bad Request');
  //       } else if (response.status === 405) {
  //         throw new Error('Concurrency Error');
          
  //       } else if (!response.ok) {
  //         // For other types of errors
  //         throw new Error('Network response was not ok');
  //       }
  //       return response.json();
  //     })
  //     .then((data) => {
  //       setVenueName(data.name);
  //       setVenueLocation(data.address);
  //       setSections(data.sections);
  //     })
  //     .catch((error) => {
  //       if (error.message === 'Concurrency Error') {
  //         alert('Concurrency Error');
  //         history.push('/ViewVenues');  // Assuming '/ViewVenue' is the correct path to navigate
  //       } else {
  //         console.error('Error fetching venue data:', error);
  //       }
  //     });
  // }, [id, token, history]);

  
  const handleSectionChange = (index, field, value) => {
    const updatedSections = [...sections];
    updatedSections[index][field] = value;
    setSections(updatedSections);
  };

  const validateCapacity = (e, capacity) => {
    if (e.key === 'Backspace' || e.key === 'Delete') {
    } else if (!/^[0-9]$/.test(e.key) || Number(capacity + e.key) > 2147483647)
      e.preventDefault();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    console.log(sections)

    // Check for empty input fields
    if (!venueName || !venueLocation || sections.some((section) => !section.type || !section.capacity)) {
      alert('Please fill in all the fields');
      return;
    }

    fetch(
      `${process.env.REACT_APP_API_URL}/update-venue`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        id: id,
        name: venueName,
        address: venueLocation,
        sections: sections,
      }),
    })
    .then(res => {
      if(res.status === 405) {
        throw new Error('Concurrency Error');
      } else if(!res.ok) {
        throw new Error('Error 400');
      } else {
        alert("Update Successfully")
        history.push("/viewUsers")
      }
    })
    .catch(err => {
      if(err.message === 'Concurrency Error') {
        alert('Concurrency Error');
      } else {
        console.error('An error occurred:', err);
      }
    });
};


  const renderSections = () => {
    return sections.map((section, index) => (
      <div key={index}>
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
          onChange={(e) => handleSectionChange(index, 'capacity', e.target.value)}
          InputLabelProps={{
            shrink: true,
          }}
        />
        <Button onClick={() => handleRemoveSection(index)}>
          <DeleteIcon fontSize="large" />
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
          Update Venue Details
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
            Update Venue
          </Button>
        </div>
      </Box>
    </div>
  );
}

export default EditVenue;
