const API_URL = "http://localhost:8080/api";

/* Load events when page opens */
window.onload = function () {
    loadEvents();
};


/* Get events from Spring Boot */
async function loadEvents() {

    try {

        const response = await fetch(`${API_URL}/events`);

        if (!response.ok) {
            throw new Error("Unable to load events");
        }

        const events = await response.json();

        const container = document.getElementById("eventContainer");

        container.innerHTML = "";

        if (events.length === 0) {

            container.innerHTML =
                "<p>No events available.</p>";

            return;
        }

        events.forEach(event => {

            const card = document.createElement("div");

            card.className = "event-card";

            card.innerHTML = `
                <h3>${event.name}</h3>

                <p>🎟️ Capacity: ${event.capacity}</p>

                <p>💰 Ticket Price: ₹${event.ticketPrice}</p>

                <button onclick="buyTicket(${event.id})">
                    Buy Ticket
                </button>
            `;

            container.appendChild(card);
        });

    } catch (error) {

        console.error(error);

        document.getElementById("eventContainer").innerHTML =
            "<p>Unable to connect to FestPass server.</p>";
    }
}


/* Purchase ticket */
async function buyTicket(eventId) {

    try {

        const response = await fetch(
            `${API_URL}/events/${eventId}/tickets`,
            {
                method: "POST"
            }
        );

        const data = await response.json();

        if (!response.ok) {

            alert(data.error || "Ticket purchase failed");

            return;
        }

        /* Show digital ticket */
        const ticketContainer =
            document.getElementById("ticketContainer");

        ticketContainer.innerHTML = `

            <h3>🎉 Ticket Purchased Successfully!</h3>

            <img src="${data.qrCode}" alt="FestPass QR Code">

            <p><strong>Ticket UUID</strong></p>

            <p class="ticket-uuid">
                ${data.uuid}
            </p>

            <p>
                📱 Show this QR code at the event entrance.
            </p>
        `;

        /* Put UUID into check-in box */
        document.getElementById("uuidInput").value =
            data.uuid;

        /* Move to ticket section */
        document.getElementById("ticket")
            .scrollIntoView({
                behavior: "smooth"
            });

    } catch (error) {

        console.error(error);

        alert("Unable to connect to FestPass server.");
    }
}


/* Validate ticket */
async function validateTicket() {

    const uuid =
        document.getElementById("uuidInput").value.trim();

    const result =
        document.getElementById("validationResult");

    if (!uuid) {

        result.innerHTML =
            "⚠️ Please enter a Ticket UUID.";

        return;
    }

    try {

        const response = await fetch(
            `${API_URL}/tickets/validate?uuid=${encodeURIComponent(uuid)}`,
            {
                method: "POST"
            }
        );

        const data = await response.json();

        if (response.ok) {

            result.innerHTML =
                "✅ " + data.message;

        } else {

            result.innerHTML =
                "❌ " + (data.error || "Ticket validation failed");
        }

    } catch (error) {

        console.error(error);

        result.innerHTML =
            "❌ Unable to connect to FestPass server.";
    }
}


/* Scroll to events */
function scrollToEvents() {

    document.getElementById("events")
        .scrollIntoView({
            behavior: "smooth"
        });
}