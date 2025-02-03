import React from 'react';
import { Alert } from 'react-bootstrap';

export default function DuplicationAlertComponent({notDup, successMessage, errorMessage, showDup, handleClose}) {
		// Alert 표시 여부를 관리하는 상태 변수

		const variant = notDup ? 'success' : 'danger';
		const message = notDup ? successMessage : errorMessage;

		if (!showDup) return null;
	return (
		<div>
			<Alert
				variant={variant}
				onClose={handleClose}
				dismissible
			>
				{message}
			</Alert>
		</div>
	)
}
