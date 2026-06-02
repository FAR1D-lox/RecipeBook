import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { Layout } from './components/Layout';
import { ProtectedRoute } from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';
import { CreateRecipePage } from './pages/CreateRecipePage';
import { EditProfilePage } from './pages/EditProfilePage';
import { EditRecipePage } from './pages/EditRecipePage';
import { FavoritesPage } from './pages/FavoritesPage';
import { HomePage } from './pages/HomePage';
import { LoginPage } from './pages/LoginPage';
import { MyProfilePage } from './pages/MyProfilePage';
import { RecommendationsPage } from './pages/RecommendationsPage';
import { RecipeDetailPage } from './pages/RecipeDetailPage';
import { RecipeSearchPage } from './pages/RecipeSearchPage';
import { RegisterPage } from './pages/RegisterPage';
import { UserProfilePage } from './pages/UserProfilePage';
import { UserSearchPage } from './pages/UserSearchPage';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route index element={<HomePage />} />
            <Route path="login" element={<LoginPage />} />
            <Route path="register" element={<RegisterPage />} />
            <Route path="users/search" element={<UserSearchPage />} />
            <Route path="users/:id" element={<UserProfilePage />} />
            <Route path="recipes/search" element={<RecipeSearchPage />} />
            <Route
              path="recipes/new"
              element={
                <ProtectedRoute>
                  <CreateRecipePage />
                </ProtectedRoute>
              }
            />
            <Route
              path="recipes/:id/edit"
              element={
                <ProtectedRoute>
                  <EditRecipePage />
                </ProtectedRoute>
              }
            />
            <Route path="recipes/:id" element={<RecipeDetailPage />} />

            <Route
              path="profile"
              element={
                <ProtectedRoute>
                  <MyProfilePage />
                </ProtectedRoute>
              }
            />
            <Route
              path="profile/edit"
              element={
                <ProtectedRoute>
                  <EditProfilePage />
                </ProtectedRoute>
              }
            />
            <Route
              path="recommendations"
              element={
                <ProtectedRoute>
                  <RecommendationsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="favorites"
              element={
                <ProtectedRoute>
                  <FavoritesPage />
                </ProtectedRoute>
              }
            />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
