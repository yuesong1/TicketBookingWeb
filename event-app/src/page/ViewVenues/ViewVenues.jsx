import React, { useEffect, useState } from 'react';
import { Button, Card, CardContent, CardHeader, Container, Grid, Typography } from '@mui/material';
import { useHistory } from 'react-router-dom';
import store from '../../component/catche/store';

const mockVenues = []

function ViewVenues() {
  const [venues, setVenues] = useState(mockVenues);
  const history = useHistory();
  const token = store.getToken();
  const role = store.getRole();

  useEffect(() => {
    fetch(`${process.env.REACT_APP_API_URL}/view-venue?mode=all`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
    .then((response) => {
      if (response.status === 405) {
        throw new Error('Concurrency Error');
      } else if (response.status === 200) {
        return response.json();
      } else {
        throw new Error('Unauthorized');
      }
    })
    .then((data) => {
      setVenues(data);
      store.setVenues(data);
    })
    .catch((error) => {
      if (error.message === 'Concurrency Error') {
        alert('Concurrency Error');
      } else {
        console.error('Error fetching venues:', error);
      }
    });
  }, [token]);


  const handleEditVenue = (venueId) => {
    // POST request to view-venue
    fetch(`${process.env.REACT_APP_API_URL}/view-venue?mode=all`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
    .then(() => {
      // GET request to update-venue
      return fetch(`${process.env.REACT_APP_API_URL}/update-venue?id=${venueId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
        }
      })
    })
    .then(response => {
      if (response.status === 400) {
        throw new Error('Bad Request on Update');
      }
      return response;
    })
    .then(() => history.push(`/EditVenue/${venueId}`))
    .catch(error => {
      if (error.message.includes('Bad Request')) {
        alert('Error 400: Bad Request');
        history.push('/ViewVenues')
      }
      console.error('Error in Edit process:', error);
    });
  };

  const handleDeleteVenue = (venueId) => {
    fetch(`${process.env.REACT_APP_API_URL}/delete-venue?id=${venueId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
    .then(res => {
      // Check if the status code is 405
      if (res.status === 405) {
        alert("Not allowed to delete venue with created event.")
        throw new Error('Method Not Allowed: DELETE operation is not supported by the server.');
      }
  
      // Ensure the response is okay, otherwise reject the promise
      if (!res.ok) {
        return res.text().then(text => {
          throw new Error(text || 'Unknown error occurred.');
        });
      }
  
      // If the response is okay, update the venues state
      setVenues((prevVenues) => prevVenues.filter((venue) => venue.id !== venueId));
    })
    .catch(err => {
      console.error(err);
      // Optionally, display an alert or notification to the user
      // alert(err.message);
    });
  };
  
  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        View Venues
      </Typography>
      <Grid container spacing={3}>
        {venues.map((venue) => (
          <Grid key={venue.id} item xs={12} sm={6} md={4}>
            <Card>
              <CardHeader title={venue.name} subheader={venue.address} />
              <CardContent>
                <Typography variant="body2" color="text.secondary">
                  Sections:
                  {venue.sections.map((section) => (
                    <div key={section.type}>
                      - {section.type} (Capacity: {section.capacity})
                    </div>
                  ))}
                </Typography>
                {role === 'admin' && (
                  <div>
                    <Button onClick={() => handleEditVenue(venue.id)}>
                      Edit
                    </Button>
                    <Button onClick={() => handleDeleteVenue(venue.id)}>
                      Delete
                    </Button>
                  </div>
                )}
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

export default ViewVenues;
