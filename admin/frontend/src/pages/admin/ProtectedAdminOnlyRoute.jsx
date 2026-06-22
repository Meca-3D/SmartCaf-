import { Navigate } from 'react-router-dom';
import { useUserStore } from '../../store/userStore';

export const ProtectedAdminOnlyRoute = ({ children }) => {
  const user = useUserStore((state) => state.user);

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (user.role !== 'ADMIN') {
    return <Navigate to="/admin" replace />;
  }

  return children;
};
