import { Navigate } from 'react-router-dom';
import { useUserStore } from '../../store/userStore';

export const ProtectedAdminRoute = ({ children }) => {
  const user = useUserStore((state) => state.user);

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (user.role !== 'ADMIN') {
    return <Navigate to="/" replace />;
  }

  return children;
};
